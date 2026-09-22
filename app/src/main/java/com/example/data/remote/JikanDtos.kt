package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JikanAnimeListResponse(
    @Json(name = "data") val data: List<JikanAnimeDto>?
)

@JsonClass(generateAdapter = true)
data class JikanSingleAnimeResponse(
    @Json(name = "data") val data: JikanAnimeDto?
)

@JsonClass(generateAdapter = true)
data class JikanAnimeDto(
    @Json(name = "mal_id") val malId: Int,
    @Json(name = "title") val title: String?,
    @Json(name = "title_japanese") val titleJapanese: String?,
    @Json(name = "synopsis") val synopsis: String?,
    @Json(name = "images") val images: JikanImagesDto?,
    @Json(name = "trailer") val trailer: JikanTrailerDto?,
    @Json(name = "score") val score: Double?,
    @Json(name = "scored_by") val scoredBy: Int?,
    @Json(name = "rank") val rank: Int?,
    @Json(name = "popularity") val popularity: Int?,
    @Json(name = "episodes") val episodes: Int?,
    @Json(name = "status") val status: String?,
    @Json(name = "rating") val rating: String?,
    @Json(name = "season") val season: String?,
    @Json(name = "year") val year: Int?,
    @Json(name = "genres") val genres: List<JikanGenreDto>?,
    @Json(name = "studios") val studios: List<JikanStudioDto>?
)

@JsonClass(generateAdapter = true)
data class JikanImagesDto(
    @Json(name = "jpg") val jpg: JikanImageSizesDto?,
    @Json(name = "webp") val webp: JikanImageSizesDto?
)

@JsonClass(generateAdapter = true)
data class JikanImageSizesDto(
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "large_image_url") val largeImageUrl: String?
)

@JsonClass(generateAdapter = true)
data class JikanTrailerDto(
    @Json(name = "youtube_id") val youtubeId: String?,
    @Json(name = "url") val url: String?
)

@JsonClass(generateAdapter = true)
data class JikanGenreDto(
    @Json(name = "mal_id") val malId: Int?,
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class JikanStudioDto(
    @Json(name = "mal_id") val malId: Int?,
    @Json(name = "name") val name: String?
)
