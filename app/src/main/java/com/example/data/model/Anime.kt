package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Anime(
    val id: Int,
    val title: String,
    val titleJapanese: String = "",
    val synopsis: String = "",
    val imageUrl: String = "",
    val bannerUrl: String = "",
    val score: Double = 0.0,
    val scoredBy: Int = 0,
    val rank: Int = 0,
    val popularity: Int = 0,
    val episodes: Int? = null,
    val status: String = "Finished Airing",
    val rating: String = "PG-13",
    val season: String = "",
    val year: Int? = null,
    val genres: List<String> = emptyList(),
    val trailerYoutubeId: String? = null,
    val studio: String = ""
)

data class Episode(
    val episodeNumber: Int,
    val title: String,
    val duration: String = "24m",
    val synopsis: String = "",
    val thumbnailUrl: String = "",
    val videoUrl: String = "",
    val isWatched: Boolean = false,
    val progressPercent: Float = 0f
)

data class AnimeCharacter(
    val name: String,
    val role: String,
    val imageUrl: String
)

enum class TimeOfDayAtmosphere(
    val displayName: String,
    val subtitle: String,
    val iconName: String
) {
    AUTO("Auto (Local)", "Synced with device time", "Schedule"),
    MORNING("Morning Glow", "Warm golden amber dawn", "WbSunny"),
    AFTERNOON("Azure Breeze", "Vibrant cobalt midday", "LightMode"),
    SUNSET("Sunset Twilight", "Faded coral dusk", "WbTwilight"),
    NIGHT("Starry Midnight", "Deep nebula amethyst", "DarkMode")
}
