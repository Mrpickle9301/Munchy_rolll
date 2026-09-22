package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeUserById(userId: Long): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET episodesWatched = episodesWatched + 1, totalWatchTimeMinutes = totalWatchTimeMinutes + :addedMinutes WHERE id = :userId")
    suspend fun incrementWatchedStats(userId: Long, addedMinutes: Int)

    @Query("UPDATE users SET preferredQuality = :quality, preferredAudio = :audio, autoPlayNext = :autoPlay WHERE id = :userId")
    suspend fun updatePreferences(userId: Long, quality: String, audio: String, autoPlay: Boolean)

    @Query("UPDATE users SET avatarId = :avatarId WHERE id = :userId")
    suspend fun updateAvatar(userId: Long, avatarId: String)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}
