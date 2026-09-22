package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Anime
import com.example.data.repository.SampleAnimeData
import com.example.ui.components.AnimePlayerDialog
import com.example.ui.components.AtmosphericBackground
import com.example.ui.components.AuthDialog
import com.example.ui.components.UserAvatarView
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.MunchyOrange
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AnimeViewModel

sealed class AppDestination {
    object Home : AppDestination()
    object Search : AppDestination()
    object Watchlist : AppDestination()
    object Profile : AppDestination()
    data class Detail(val anime: Anime) : AppDestination()
}

@Composable
fun MunchyrollApp(
    viewModel: AnimeViewModel = viewModel()
) {
    var currentDestination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }

    val atmosphere by viewModel.timeAtmosphere.collectAsStateWithLifecycle()
    val trendingAnime by viewModel.trendingAnime.collectAsStateWithLifecycle()
    val seasonalAnime by viewModel.seasonalAnime.collectAsStateWithLifecycle()
    val topAnime by viewModel.topAnime.collectAsStateWithLifecycle()
    val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()
    val selectedAnime by viewModel.selectedAnime.collectAsStateWithLifecycle()
    val episodes by viewModel.episodes.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    // Auth state
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isAuthLoading by viewModel.isAuthLoading.collectAsStateWithLifecycle()
    val authErrorMessage by viewModel.authErrorMessage.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val authDialogIsSignUp by viewModel.authDialogIsSignUp.collectAsStateWithLifecycle()

    // Player State
    val activePlayingEpisode by viewModel.activePlayingEpisode.collectAsStateWithLifecycle()
    val isPlayingVideo by viewModel.isPlayingVideo.collectAsStateWithLifecycle()
    val playbackProgress by viewModel.playbackProgress.collectAsStateWithLifecycle()
    val videoQuality by viewModel.videoQuality.collectAsStateWithLifecycle()
    val audioTrack by viewModel.audioTrack.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()

    // Handle system back navigation
    BackHandler(enabled = currentDestination is AppDestination.Detail) {
        currentDestination = AppDestination.Home
    }

    AtmosphericBackground(atmosphere = atmosphere) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                if (currentDestination !is AppDestination.Detail) {
                    NavigationBar(
                        containerColor = SurfaceCard.copy(alpha = 0.95f),
                        contentColor = MunchyOrange,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .testTag("bottom_nav_bar")
                            .navigationBarsPadding()
                    ) {
                        // Home / Discover
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Home,
                            onClick = { currentDestination = AppDestination.Home },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = "Discover"
                                )
                            },
                            label = { Text("Discover") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MunchyOrange,
                                selectedTextColor = MunchyOrange,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = MunchyOrange.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag("nav_home_tab")
                        )

                        // Search
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Search,
                            onClick = { currentDestination = AppDestination.Search },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            },
                            label = { Text("Search") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MunchyOrange,
                                selectedTextColor = MunchyOrange,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = MunchyOrange.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag("nav_search_tab")
                        )

                        // Watchlist
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Watchlist,
                            onClick = { currentDestination = AppDestination.Watchlist },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (watchlist.isNotEmpty()) {
                                            Badge(
                                                containerColor = MunchyOrange,
                                                contentColor = Color.White
                                            ) {
                                                Text("${watchlist.size}")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (currentDestination is AppDestination.Watchlist)
                                            Icons.Default.Bookmark
                                        else
                                            Icons.Default.BookmarkBorder,
                                        contentDescription = "Watchlist"
                                    )
                                }
                            },
                            label = { Text("Watchlist") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MunchyOrange,
                                selectedTextColor = MunchyOrange,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = MunchyOrange.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag("nav_watchlist_tab")
                        )

                        // Account / Profile
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Profile,
                            onClick = { currentDestination = AppDestination.Profile },
                            icon = {
                                if (currentUser != null) {
                                    UserAvatarView(
                                        avatarId = currentUser!!.avatarId,
                                        size = 24.dp,
                                        iconSize = 14.dp,
                                        showBorder = currentDestination is AppDestination.Profile
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (currentDestination is AppDestination.Profile)
                                            Icons.Default.Person
                                        else
                                            Icons.Default.PersonOutline,
                                        contentDescription = "Account"
                                    )
                                }
                            },
                            label = { Text("Account") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MunchyOrange,
                                selectedTextColor = MunchyOrange,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = MunchyOrange.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag("nav_profile_tab")
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ScreenTransition"
                ) { destination ->
                    when (destination) {
                        is AppDestination.Home -> {
                            HomeScreen(
                                trendingAnime = trendingAnime,
                                seasonalAnime = seasonalAnime,
                                topAnime = topAnime,
                                watchlist = watchlist,
                                timeAtmosphere = atmosphere,
                                onSelectAtmosphere = { viewModel.setTimeAtmosphere(it) },
                                onSelectAnime = { anime ->
                                    viewModel.selectAnime(anime)
                                    currentDestination = AppDestination.Detail(anime)
                                },
                                onQuickPlay = { anime ->
                                    viewModel.selectAnime(anime)
                                    val firstEp = SampleAnimeData.getEpisodesForAnime(anime).first()
                                    viewModel.startStreaming(firstEp)
                                },
                                onResumeWatchlist = { entry ->
                                    viewModel.selectAnimeById(entry.animeId)
                                    val eps = SampleAnimeData.getEpisodesForAnime(
                                        selectedAnime ?: SampleAnimeData.curatedAnimeList.first()
                                    )
                                    val targetEp = eps.firstOrNull { it.episodeNumber == entry.currentEpisode }
                                        ?: eps.first()
                                    viewModel.startStreaming(targetEp)
                                },
                                onToggleWatchlist = { viewModel.toggleWatchlist(it) },
                                isInWatchlist = { viewModel.isAnimeInWatchlist(it) },
                                onRefresh = { viewModel.loadHomeData() },
                                currentUser = currentUser,
                                onUserAvatarClick = { currentDestination = AppDestination.Profile }
                            )
                        }

                        is AppDestination.Search -> {
                            SearchScreen(
                                searchQuery = searchQuery,
                                onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                                selectedGenre = selectedGenre,
                                onGenreSelected = { viewModel.onGenreSelected(it) },
                                genresList = viewModel.genresList,
                                results = searchResults,
                                isSearching = isSearching,
                                onSelectAnime = { anime ->
                                    viewModel.selectAnime(anime)
                                    currentDestination = AppDestination.Detail(anime)
                                }
                            )
                        }

                        is AppDestination.Watchlist -> {
                            WatchlistScreen(
                                watchlist = watchlist,
                                onSelectAnimeById = { animeId ->
                                    viewModel.selectAnimeById(animeId)
                                    selectedAnime?.let { anime ->
                                        currentDestination = AppDestination.Detail(anime)
                                    }
                                },
                                onResumeAnime = { entry ->
                                    viewModel.selectAnimeById(entry.animeId)
                                    val eps = SampleAnimeData.getEpisodesForAnime(
                                        selectedAnime ?: SampleAnimeData.curatedAnimeList.first()
                                    )
                                    val targetEp = eps.firstOrNull { it.episodeNumber == entry.currentEpisode }
                                        ?: eps.first()
                                    viewModel.startStreaming(targetEp)
                                },
                                onRemoveFromWatchlist = { animeId ->
                                    viewModel.removeFromWatchlist(animeId)
                                },
                                onExploreClicked = {
                                    currentDestination = AppDestination.Home
                                }
                            )
                        }

                        is AppDestination.Profile -> {
                            ProfileScreen(
                                currentUser = currentUser,
                                watchlistCount = watchlist.size,
                                onOpenAuthDialog = { viewModel.openAuthDialog(false) },
                                onLogOut = { viewModel.logOut() },
                                onSelectAvatar = { viewModel.updateUserAvatar(it) },
                                onUpdatePreferences = { q, a, auto -> viewModel.updateUserPreferences(q, a, auto) }
                            )
                        }

                        is AppDestination.Detail -> {
                            val activeAnime = selectedAnime ?: destination.anime
                            val chars = SampleAnimeData.getCharactersForAnime(activeAnime.id)

                            DetailScreen(
                                anime = activeAnime,
                                episodes = episodes,
                                characters = chars,
                                isInWatchlist = viewModel.isAnimeInWatchlist(activeAnime.id),
                                onBack = { currentDestination = AppDestination.Home },
                                onPlayEpisode = { ep -> viewModel.startStreaming(ep) },
                                onToggleWatchlist = { viewModel.toggleWatchlist(it) }
                            )
                        }
                    }
                }

                // Video Streaming Player Modal
                if (activePlayingEpisode != null && selectedAnime != null) {
                    AnimePlayerDialog(
                        anime = selectedAnime!!,
                        episode = activePlayingEpisode!!,
                        allEpisodes = episodes,
                        isPlaying = isPlayingVideo,
                        progress = playbackProgress,
                        quality = videoQuality,
                        audioTrack = audioTrack,
                        speed = playbackSpeed,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onSeek = { viewModel.seekTo(it) },
                        onSkipForward = { viewModel.skipForward10s() },
                        onSkipBackward = { viewModel.skipBackward10s() },
                        onSelectQuality = { viewModel.setVideoQuality(it) },
                        onSelectAudio = { viewModel.setAudioTrack(it) },
                        onSelectSpeed = { viewModel.setPlaybackSpeed(it) },
                        onSwitchEpisode = { viewModel.startStreaming(it) },
                        onClose = { viewModel.stopStreaming() }
                    )
                }

                // User Authentication Modal Dialog
                if (showAuthDialog) {
                    AuthDialog(
                        initialIsSignUp = authDialogIsSignUp,
                        errorMessage = authErrorMessage,
                        isLoading = isAuthLoading,
                        onSignIn = { email, pass -> viewModel.signIn(email, pass) },
                        onSignUp = { username, email, pass -> viewModel.signUp(username, email, pass) },
                        onClearError = { viewModel.clearAuthError() },
                        onDismiss = { viewModel.dismissAuthDialog() }
                    )
                }
            }
        }
    }
}
