package com.example.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.regex.Pattern

object SecurityUtils {

    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}$"
    )

    fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        // Apply salt with 1000 rounds of hashing for defense against brute-force
        var hash = digest.digest((salt + password).toByteArray(Charsets.UTF_8))
        for (i in 1..1000) {
            digest.reset()
            hash = digest.digest(hash)
        }
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val computedHash = hashPassword(password, salt)
        return computedHash == expectedHash
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_PATTERN.matcher(email.trim()).matches()
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username is required"
            username.trim().length < 3 -> "Username must be at least 3 characters"
            username.length > 24 -> "Username must be 24 characters or less"
            else -> null
        }
    }
}
