package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.SessionManager
import com.example.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class UserAuthRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        sessionManager = SessionManager(context)
        sessionManager.clearSession()
        userRepository = UserRepository(database.userDao(), sessionManager)
    }

    @After
    fun tearDown() {
        sessionManager.clearSession()
        database.close()
    }

    @Test
    fun testUserSignUpAndLogin() = runBlocking {
        val signUpResult = userRepository.signUp(
            username = "SasukeUchiha",
            email = "sasuke@leafvillage.org",
            password = "ChidoriPower99!"
        )

        assertTrue(signUpResult.isSuccess)
        val user = signUpResult.getOrThrow()
        assertEquals("SasukeUchiha", user.username)
        assertEquals("sasuke@leafvillage.org", user.email)
        assertTrue(user.isBetaTester)
        assertEquals(user.id, sessionManager.activeUserId.value)

        // Log out
        userRepository.logOut()
        assertNull(sessionManager.activeUserId.value)

        // Log back in with correct credentials
        val loginResult = userRepository.logIn(
            email = "sasuke@leafvillage.org",
            password = "ChidoriPower99!"
        )
        assertTrue(loginResult.isSuccess)
        assertEquals(user.id, loginResult.getOrThrow().id)

        // Log in with wrong password
        val failedLogin = userRepository.logIn(
            email = "sasuke@leafvillage.org",
            password = "WrongPassword"
        )
        assertTrue(failedLogin.isFailure)

        // Sign up with duplicate email should fail
        val duplicateSignUp = userRepository.signUp(
            username = "CloneSasuke",
            email = "sasuke@leafvillage.org",
            password = "AnotherPassword123"
        )
        assertTrue(duplicateSignUp.isFailure)
    }

    @Test
    fun testPreferencesAndAvatarUpdate() = runBlocking {
        val user = userRepository.signUp(
            username = "NarutoUzumaki",
            email = "naruto@leafvillage.org",
            password = "RasenganHero123!"
        ).getOrThrow()

        userRepository.updateAvatar(user.id, "avatar_3")
        val updated = database.userDao().getUserById(user.id)
        assertNotNull(updated)
        assertEquals("avatar_3", updated?.avatarId)

        userRepository.updatePreferences(user.id, "720p HD", "English (Dub)", false)
        val prefUpdated = database.userDao().getUserById(user.id)
        assertEquals("720p HD", prefUpdated?.preferredQuality)
        assertEquals("English (Dub)", prefUpdated?.preferredAudio)
        assertEquals(false, prefUpdated?.autoPlayNext)
    }
}
