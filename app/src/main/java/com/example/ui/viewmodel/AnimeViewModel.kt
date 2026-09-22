package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SessionManager
import com.example.data.local.UserEntity
import com.example.data.local.WatchlistEntity
import com.example.data.model.Anime
import com.example.data.model.Episode
import com.example.data.model.TimeOfDayAtmosphere
import com.example.data.repository.AnimeRepository
import com.example.data.repository.SampleAnimeData
import com.example.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnimeRepository
    private val userRepository: UserRepository

    // Auth & User States
    val currentUser: StateFlow<UserEntity?>
    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    private val _authDialogIsSignUp = MutableStateFlow(false)
    val authDialogIsSignUp: StateFlow<Boolean> = _authDialogIsSignUp.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = AnimeRepository(db.watchlistDao())
        val sessionManager = SessionManager.getInstance(application)
        userRepository = UserRepository(db.userDao(), sessionManager)
        currentUser = userRepository.currentUser
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

        // Seed demo account on first run
        viewModelScope.launch {
            userRepository.seedDemoUserIfEmpty()
        }
    }

    // UI States
    private val _timeAtmosphere = MutableStateFlow(TimeOfDayAtmosphere.AUTO)
    val timeAtmosphere: StateFlow<TimeOfDayAtmosphere> = _timeAtmosphere.asStateFlow()

    private val _trendingAnime = MutableStateFlow<List<Anime>>(SampleAnimeData.curatedAnimeList)
    val trendingAnime: StateFlow<List<Anime>> = _trendingAnime.asStateFlow()

    private val _seasonalAnime = MutableStateFlow<List<Anime>>(SampleAnimeData.curatedAnimeList.take(6))
    val seasonalAnime: StateFlow<List<Anime>> = _seasonalAnime.asStateFlow()

    private val _topAnime = MutableStateFlow<List<Anime>>(SampleAnimeData.curatedAnimeList.sortedByDescending { it.score })
    val topAnime: StateFlow<List<Anime>> = _topAnime.asStateFlow()

    private val _isLoadingHome = MutableStateFlow(false)
    val isLoadingHome: StateFlow<Boolean> = _isLoadingHome.asStateFlow()

    // Selected Anime Details
    private val _selectedAnime = MutableStateFlow<Anime?>(SampleAnimeData.curatedAnimeList.first())
    val selectedAnime: StateFlow<Anime?> = _selectedAnime.asStateFlow()

    private val _episodes = MutableStateFlow<List<Episode>>(emptyList())
    val episodes: StateFlow<List<Episode>> = _episodes.asStateFlow()

    // Search & Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Anime>>(SampleAnimeData.curatedAnimeList)
    val searchResults: StateFlow<List<Anime>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Watchlist from Room DB
    val watchlist: StateFlow<List<WatchlistEntity>> = repository.allWatchlist
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Player State
    private val _activePlayingEpisode = MutableStateFlow<Episode?>(null)
    val activePlayingEpisode: StateFlow<Episode?> = _activePlayingEpisode.asStateFlow()

    private val _isPlayingVideo = MutableStateFlow(false)
    val isPlayingVideo: StateFlow<Boolean> = _isPlayingVideo.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0.35f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _videoQuality = MutableStateFlow("1080p HD")
    val videoQuality: StateFlow<String> = _videoQuality.asStateFlow()

    private val _audioTrack = MutableStateFlow("Japanese (Original)")
    val audioTrack: StateFlow<String> = _audioTrack.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private var searchDebounceJob: Job? = null
    private var playbackTickerJob: Job? = null

    val genresList = listOf(
        "All", "Action", "Adventure", "Fantasy", "Supernatural",
        "Comedy", "Drama", "Sci-Fi", "Mystery", "Slice of Life"
    )

    init {
        loadHomeData()
        selectAnime(SampleAnimeData.curatedAnimeList.first())
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _isLoadingHome.value = true
            try {
                val trending = repository.getTrendingAnime()
                _trendingAnime.value = trending

                val seasonal = repository.getSeasonalAnime()
                _seasonalAnime.value = seasonal

                val top = repository.getTopAnime()
                _topAnime.value = top
            } catch (e: Exception) {
                // keep local fallbacks
            } finally {
                _isLoadingHome.value = false
            }
        }
    }

    fun selectAnime(anime: Anime) {
        _selectedAnime.value = anime
        _episodes.value = repository.getEpisodes(anime)
    }

    fun selectAnimeById(animeId: Int) {
        val found = _trendingAnime.value.firstOrNull { it.id == animeId }
            ?: _topAnime.value.firstOrNull { it.id == animeId }
            ?: SampleAnimeData.curatedAnimeList.firstOrNull { it.id == animeId }

        if (found != null) {
            selectAnime(found)
        } else {
            viewModelScope.launch {
                val fetched = repository.getAnimeById(animeId)
                selectAnime(fetched)
            }
        }
    }

    fun setTimeAtmosphere(atmosphere: TimeOfDayAtmosphere) {
        _timeAtmosphere.value = atmosphere
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            _isSearching.value = true
            delay(350)
            executeSearch()
            _isSearching.value = false
        }
    }

    fun onGenreSelected(genre: String) {
        _selectedGenre.value = genre
        viewModelScope.launch {
            _isSearching.value = true
            executeSearch()
            _isSearching.value = false
        }
    }

    private suspend fun executeSearch() {
        val results = repository.searchAnime(_searchQuery.value, _selectedGenre.value)
        _searchResults.value = results
    }

    // Video Player Controls
    fun startStreaming(episode: Episode) {
        _activePlayingEpisode.value = episode
        _isPlayingVideo.value = true
        _playbackProgress.value = if (episode.progressPercent > 0) episode.progressPercent else 0.05f
        startPlaybackSimulation()

        // Auto-add to watchlist as "WATCHING"
        _selectedAnime.value?.let { anime ->
            viewModelScope.launch {
                repository.addToWatchlist(
                    anime = anime,
                    status = "WATCHING",
                    currentEpisode = episode.episodeNumber,
                    progressMs = (_playbackProgress.value * 1440000L).toLong()
                )
            }
        }
    }

    fun stopStreaming() {
        playbackTickerJob?.cancel()
        _isPlayingVideo.value = false
        _activePlayingEpisode.value = null
    }

    fun togglePlayPause() {
        _isPlayingVideo.value = !_isPlayingVideo.value
        if (_isPlayingVideo.value) {
            startPlaybackSimulation()
        } else {
            playbackTickerJob?.cancel()
        }
    }

    fun seekTo(fraction: Float) {
        val clamped = fraction.coerceIn(0f, 1f)
        _playbackProgress.value = clamped
        _selectedAnime.value?.let { anime ->
            _activePlayingEpisode.value?.let { ep ->
                viewModelScope.launch {
                    repository.updateWatchProgress(
                        animeId = anime.id,
                        episode = ep.episodeNumber,
                        progressMs = (clamped * 1440000L).toLong()
                    )
                }
            }
        }
    }

    fun skipForward10s() {
        // 10s of 24m is ~0.007
        seekTo(_playbackProgress.value + 0.007f)
    }

    fun skipBackward10s() {
        seekTo(_playbackProgress.value - 0.007f)
    }

    fun setVideoQuality(quality: String) {
        _videoQuality.value = quality
    }

    fun setAudioTrack(track: String) {
        _audioTrack.value = track
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    private fun startPlaybackSimulation() {
        playbackTickerJob?.cancel()
        playbackTickerJob = viewModelScope.launch {
            while (_isPlayingVideo.value) {
                delay(1000)
                val current = _playbackProgress.value
                val step = 0.0007f * _playbackSpeed.value
                if (current + step >= 1.0f) {
                    _playbackProgress.value = 1.0f
                    _isPlayingVideo.value = false
                    recordWatchEpisodeCompleted()
                    break
                } else {
                    _playbackProgress.value = current + step
                }
            }
        }
    }

    // Watchlist Actions
    fun toggleWatchlist(anime: Anime, targetStatus: String = "WATCHING") {
        viewModelScope.launch {
            val existing = watchlist.value.firstOrNull { it.animeId == anime.id }
            if (existing != null) {
                repository.removeFromWatchlist(anime.id)
            } else {
                repository.addToWatchlist(anime, targetStatus)
            }
        }
    }

    fun isAnimeInWatchlist(animeId: Int): Boolean {
        return watchlist.value.any { it.animeId == animeId }
    }

    fun removeFromWatchlist(animeId: Int) {
        viewModelScope.launch {
            repository.removeFromWatchlist(animeId)
        }
    }

    // User Authentication Actions
    fun openAuthDialog(isSignUp: Boolean = false) {
        _authDialogIsSignUp.value = isSignUp
        _authErrorMessage.value = null
        _showAuthDialog.value = true
    }

    fun dismissAuthDialog() {
        _showAuthDialog.value = false
        _authErrorMessage.value = null
    }

    fun clearAuthError() {
        _authErrorMessage.value = null
    }

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            _isAuthLoading.value = true
            _authErrorMessage.value = null
            try {
                val result = userRepository.logIn(email, pass)
                result.fold(
                    onSuccess = { user ->
                        _isAuthLoading.value = false
                        _showAuthDialog.value = false
                        // Sync preference defaults
                        _videoQuality.value = user.preferredQuality
                    },
                    onFailure = { error ->
                        _isAuthLoading.value = false
                        _authErrorMessage.value = error.message ?: "Authentication failed"
                    }
                )
            } catch (e: Exception) {
                _isAuthLoading.value = false
                _authErrorMessage.value = e.message ?: "An unexpected error occurred"
            }
        }
    }

    fun signUp(username: String, email: String, pass: String) {
        viewModelScope.launch {
            _isAuthLoading.value = true
            _authErrorMessage.value = null
            try {
                val result = userRepository.signUp(username, email, pass)
                result.fold(
                    onSuccess = { user ->
                        _isAuthLoading.value = false
                        _showAuthDialog.value = false
                        _videoQuality.value = user.preferredQuality
                    },
                    onFailure = { error ->
                        _isAuthLoading.value = false
                        _authErrorMessage.value = error.message ?: "Registration failed"
                    }
                )
            } catch (e: Exception) {
                _isAuthLoading.value = false
                _authErrorMessage.value = e.message ?: "An unexpected error occurred"
            }
        }
    }

    fun logOut() {
        userRepository.logOut()
    }

    fun updateUserAvatar(avatarId: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            userRepository.updateAvatar(user.id, avatarId)
        }
    }

    fun updateUserPreferences(quality: String, audio: String, autoPlay: Boolean) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            userRepository.updatePreferences(user.id, quality, audio, autoPlay)
            _videoQuality.value = quality
            _audioTrack.value = audio
        }
    }

    fun recordWatchEpisodeCompleted() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            userRepository.recordWatchActivity(user.id, 24)
        }
    }
}
