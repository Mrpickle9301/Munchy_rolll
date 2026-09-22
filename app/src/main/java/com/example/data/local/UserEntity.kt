package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val username: String,
    val passwordHash: String,
    val salt: String,
    val avatarId: String = "avatar_1",
    val memberSince: Long = System.currentTimeMillis(),
    val isBetaTester: Boolean = true,
    val episodesWatched: Int = 0,
    val totalWatchTimeMinutes: Int = 0,
    val preferredQuality: String = "1080p HD",
    val preferredAudio: String = "Japanese (Sub)",
    val autoPlayNext: Boolean = true
)
