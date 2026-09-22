package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface JikanApiService {

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("filter") filter: String = "bypopularity",
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): JikanAnimeListResponse

    @GET("seasons/now")
    suspend fun getSeasonalAnime(
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): JikanAnimeListResponse

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("sfw") sfw: Boolean = true,
        @Query("limit") limit: Int = 20,
        @Query("order_by") orderBy: String = "popularity",
        @Query("sort") sort: String = "asc"
    ): JikanAnimeListResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") animeId: Int
    ): JikanSingleAnimeResponse
}

object NetworkClient {
    private const val BASE_URL = "https://api.jikan.moe/v4/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: JikanApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(JikanApiService::class.java)
    }
}
