package com.example.data.repository

import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.model.Anime
import com.example.data.model.AnimeCharacter
import com.example.data.model.Episode
import com.example.data.remote.JikanAnimeDto
import com.example.data.remote.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val watchlistDao: WatchlistDao
) {
    private val apiService = NetworkClient.apiService

    suspend fun getTrendingAnime(): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTopAnime(filter = "bypopularity", limit = 15)
            val list = response.data?.mapNotNull { it.toDomainAnime() }
            if (!list.isNullOrEmpty()) list else SampleAnimeData.curatedAnimeList
        } catch (e: Exception) {
            SampleAnimeData.curatedAnimeList
        }
    }

    suspend fun getSeasonalAnime(): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSeasonalAnime(limit = 15)
            val list = response.data?.mapNotNull { it.toDomainAnime() }
            if (!list.isNullOrEmpty()) list else SampleAnimeData.curatedAnimeList.take(6)
        } catch (e: Exception) {
            SampleAnimeData.curatedAnimeList.take(6)
        }
    }

    suspend fun getTopAnime(): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTopAnime(filter = "favorite", limit = 15)
            val list = response.data?.mapNotNull { it.toDomainAnime() }
            if (!list.isNullOrEmpty()) list else SampleAnimeData.curatedAnimeList.sortedByDescending { it.score }
        } catch (e: Exception) {
            SampleAnimeData.curatedAnimeList.sortedByDescending { it.score }
        }
    }

    suspend fun searchAnime(query: String, genre: String? = null): List<Anime> = withContext(Dispatchers.IO) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty()) {
            try {
                val response = apiService.searchAnime(query = trimmedQuery, limit = 20)
                val liveResults = response.data?.mapNotNull { it.toDomainAnime() }
                if (!liveResults.isNullOrEmpty()) {
                    return@withContext if (genre != null && genre != "All") {
                        liveResults.filter { it.genres.any { g -> g.contains(genre, ignoreCase = true) } }
                    } else {
                        liveResults
                    }
                }
            } catch (e: Exception) {
                // fall through to local search
            }
        }

        // Local search filter
        SampleAnimeData.curatedAnimeList.filter { anime ->
            val matchesQuery = trimmedQuery.isEmpty() ||
                anime.title.contains(trimmedQuery, ignoreCase = true) ||
                anime.titleJapanese.contains(trimmedQuery, ignoreCase = true) ||
                anime.synopsis.contains(trimmedQuery, ignoreCase = true)

            val matchesGenre = genre == null || genre == "All" ||
                anime.genres.any { it.contains(genre, ignoreCase = true) }

            matchesQuery && matchesGenre
        }
    }

    suspend fun getAnimeById(animeId: Int): Anime = withContext(Dispatchers.IO) {
        val local = SampleAnimeData.curatedAnimeList.firstOrNull { it.id == animeId }
        if (local != null) return@withContext local

        try {
            val response = apiService.getAnimeDetails(animeId)
            response.data?.toDomainAnime() ?: SampleAnimeData.curatedAnimeList.first()
        } catch (e: Exception) {
            SampleAnimeData.curatedAnimeList.first()
        }
    }

    fun getEpisodes(anime: Anime): List<Episode> {
        return SampleAnimeData.getEpisodesForAnime(anime)
    }

    fun getCharacters(animeId: Int): List<AnimeCharacter> {
        return SampleAnimeData.getCharactersForAnime(animeId)
    }

    // --- Watchlist / Room Persistence ---
    val allWatchlist: Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()

    fun getWatchlistEntry(animeId: Int): Flow<WatchlistEntity?> {
        return watchlistDao.getWatchlistEntry(animeId)
    }

    suspend fun addToWatchlist(
        anime: Anime,
        status: String = "WATCHING",
        currentEpisode: Int = 1,
        progressMs: Long = 0L
    ) = withContext(Dispatchers.IO) {
        val entity = WatchlistEntity(
            animeId = anime.id,
            title = anime.title,
            imageUrl = anime.imageUrl,
            score = anime.score,
            totalEpisodes = anime.episodes,
            currentEpisode = currentEpisode,
            progressMs = progressMs,
            status = status,
            genres = anime.genres.joinToString(", ")
        )
        watchlistDao.insertOrUpdate(entity)
    }

    suspend fun updateWatchProgress(animeId: Int, episode: Int, progressMs: Long) = withContext(Dispatchers.IO) {
        val existing = watchlistDao.getWatchlistEntrySync(animeId)
        if (existing != null) {
            watchlistDao.updateProgress(animeId, episode, progressMs)
        }
    }

    suspend fun removeFromWatchlist(animeId: Int) = withContext(Dispatchers.IO) {
        watchlistDao.deleteByAnimeId(animeId)
    }

    private fun JikanAnimeDto.toDomainAnime(): Anime? {
        val id = malId
        val t = title ?: return null
        val img = images?.webp?.largeImageUrl
            ?: images?.jpg?.largeImageUrl
            ?: images?.webp?.imageUrl
            ?: images?.jpg?.imageUrl
            ?: ""

        val genresList = genres?.mapNotNull { it.name } ?: emptyList()
        val studioName = studios?.firstOrNull()?.name ?: ""

        return Anime(
            id = id,
            title = t,
            titleJapanese = titleJapanese ?: "",
            synopsis = synopsis ?: "No synopsis available.",
            imageUrl = img,
            bannerUrl = img,
            score = score ?: 0.0,
            scoredBy = scoredBy ?: 0,
            rank = rank ?: 0,
            popularity = popularity ?: 0,
            episodes = episodes,
            status = status ?: "Unknown",
            rating = rating ?: "PG-13",
            season = season ?: "",
            year = year,
            genres = genresList,
            trailerYoutubeId = trailer?.youtubeId,
            studio = studioName
        )
    }
}
