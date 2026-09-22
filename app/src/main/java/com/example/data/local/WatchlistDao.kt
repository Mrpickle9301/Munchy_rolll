package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY updatedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE status = :status ORDER BY updatedAt DESC")
    fun getWatchlistByStatus(status: String): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE animeId = :animeId LIMIT 1")
    fun getWatchlistEntry(animeId: Int): Flow<WatchlistEntity?>

    @Query("SELECT * FROM watchlist WHERE animeId = :animeId LIMIT 1")
    suspend fun getWatchlistEntrySync(animeId: Int): WatchlistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE animeId = :animeId")
    suspend fun deleteByAnimeId(animeId: Int)

    @Query("UPDATE watchlist SET currentEpisode = :episode, progressMs = :progressMs, updatedAt = :timestamp WHERE animeId = :animeId")
    suspend fun updateProgress(animeId: Int, episode: Int, progressMs: Long, timestamp: Long = System.currentTimeMillis())
}
