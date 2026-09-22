package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val animeId: Int,
    val title: String,
    val imageUrl: String,
    val score: Double,
    val totalEpisodes: Int?,
    val currentEpisode: Int = 1,
    val progressMs: Long = 0L,
    val totalDurationMs: Long = 1440000L, // default 24m
    val status: String, // "WATCHING", "PLAN_TO_WATCH", "COMPLETED"
    val genres: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
