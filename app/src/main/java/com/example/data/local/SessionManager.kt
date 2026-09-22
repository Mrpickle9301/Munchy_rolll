package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _activeUserId = MutableStateFlow<Long?>(readActiveUserId())
    val activeUserId: StateFlow<Long?> = _activeUserId.asStateFlow()

    private fun readActiveUserId(): Long? {
        val id = prefs.getLong(KEY_USER_ID, -1L)
        return if (id != -1L) id else null
    }

    fun saveSession(userId: Long) {
        prefs.edit()
            .putLong(KEY_USER_ID, userId)
            .apply()
        _activeUserId.value = userId
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .apply()
        _activeUserId.value = null
    }

    fun isLoggedIn(): Boolean {
        return _activeUserId.value != null
    }

    companion object {
        private const val PREFS_NAME = "munchyroll_session_prefs"
        private const val KEY_USER_ID = "active_user_id"

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
