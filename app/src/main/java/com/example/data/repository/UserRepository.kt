package com.example.data.repository

import com.example.data.local.SessionManager
import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import com.example.util.SecurityUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {

    val currentUser: Flow<UserEntity?> = sessionManager.activeUserId.flatMapLatest { userId ->
        if (userId != null) {
            userDao.observeUserById(userId)
        } else {
            flowOf(null)
        }
    }

    suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): Result<UserEntity> {
        val trimmedEmail = email.trim()
        val trimmedUsername = username.trim()

        val usernameErr = SecurityUtils.validateUsername(trimmedUsername)
        if (usernameErr != null) {
            return Result.failure(IllegalArgumentException(usernameErr))
        }

        if (!SecurityUtils.isValidEmail(trimmedEmail)) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }

        val passwordErr = SecurityUtils.validatePassword(password)
        if (passwordErr != null) {
            return Result.failure(IllegalArgumentException(passwordErr))
        }

        val existingUser = userDao.getUserByEmail(trimmedEmail)
        if (existingUser != null) {
            return Result.failure(IllegalArgumentException("An account with this email already exists"))
        }

        val salt = SecurityUtils.generateSalt()
        val passwordHash = SecurityUtils.hashPassword(password, salt)

        val newUser = UserEntity(
            email = trimmedEmail,
            username = trimmedUsername,
            passwordHash = passwordHash,
            salt = salt,
            avatarId = "avatar_${(1..6).random()}",
            memberSince = System.currentTimeMillis(),
            isBetaTester = true
        )

        val newId = userDao.insertUser(newUser)
        val created = newUser.copy(id = newId)

        sessionManager.saveSession(newId)
        return Result.success(created)
    }

    suspend fun logIn(email: String, password: String): Result<UserEntity> {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email is required"))
        }
        if (password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Password is required"))
        }

        val user = userDao.getUserByEmail(trimmedEmail)
            ?: return Result.failure(IllegalArgumentException("No account found with this email"))

        val isPasswordCorrect = SecurityUtils.verifyPassword(password, user.salt, user.passwordHash)
        if (!isPasswordCorrect) {
            return Result.failure(IllegalArgumentException("Incorrect password. Please try again."))
        }

        sessionManager.saveSession(user.id)
        return Result.success(user)
    }

    fun logOut() {
        sessionManager.clearSession()
    }

    suspend fun updateAvatar(userId: Long, avatarId: String) {
        userDao.updateAvatar(userId, avatarId)
    }

    suspend fun updatePreferences(
        userId: Long,
        quality: String,
        audio: String,
        autoPlay: Boolean
    ) {
        userDao.updatePreferences(userId, quality, audio, autoPlay)
    }

    suspend fun recordWatchActivity(userId: Long, minutes: Int) {
        userDao.incrementWatchedStats(userId, minutes)
    }

    suspend fun seedDemoUserIfEmpty(): UserEntity? {
        val count = userDao.getUserCount()
        if (count == 0) {
            val salt = SecurityUtils.generateSalt()
            val hash = SecurityUtils.hashPassword("MunchyPass123!", salt)
            val demoUser = UserEntity(
                email = "otaku_hero@munchyroll.tv",
                username = "AnimeMaster",
                passwordHash = hash,
                salt = salt,
                avatarId = "avatar_1",
                memberSince = System.currentTimeMillis() - (14L * 24 * 60 * 60 * 1000), // 2 weeks ago
                isBetaTester = true,
                episodesWatched = 18,
                totalWatchTimeMinutes = 432,
                preferredQuality = "1080p HD",
                preferredAudio = "Japanese (Sub)",
                autoPlayNext = true
            )
            val id = userDao.insertUser(demoUser)
            val inserted = demoUser.copy(id = id)
            // Auto login to demo user on first launch for seamless initial experience
            sessionManager.saveSession(id)
            return inserted
        }
        return null
    }
}
