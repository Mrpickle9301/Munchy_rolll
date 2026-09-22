package com.example

import com.example.util.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthAndSecurityUnitTest {

    @Test
    fun testPasswordHashingAndVerification() {
        val password = "StrongPassword123!"
        val salt = SecurityUtils.generateSalt()

        // Salt should not be empty
        assertTrue(salt.isNotEmpty())
        assertEquals(32, salt.length) // 16 bytes = 32 hex chars

        val hash = SecurityUtils.hashPassword(password, salt)
        assertTrue(hash.isNotEmpty())

        // Verification with correct password
        val isValid = SecurityUtils.verifyPassword(password, salt, hash)
        assertTrue(isValid)

        // Verification with wrong password
        val isInvalid = SecurityUtils.verifyPassword("WrongPassword", salt, hash)
        assertFalse(isInvalid)

        // Two different salts should produce different hashes for same password
        val salt2 = SecurityUtils.generateSalt()
        val hash2 = SecurityUtils.hashPassword(password, salt2)
        assertFalse(hash == hash2)
    }

    @Test
    fun testEmailValidation() {
        assertTrue(SecurityUtils.isValidEmail("otaku@munchyroll.tv"))
        assertTrue(SecurityUtils.isValidEmail("user.name+tag@domain.co.uk"))
        assertFalse(SecurityUtils.isValidEmail("invalid-email"))
        assertFalse(SecurityUtils.isValidEmail("@missingusername.com"))
        assertFalse(SecurityUtils.isValidEmail("missingdomain@"))
        assertFalse(SecurityUtils.isValidEmail(""))
    }

    @Test
    fun testPasswordValidation() {
        assertNull(SecurityUtils.validatePassword("validPass123"))
        assertNotNull(SecurityUtils.validatePassword(""))
        assertNotNull(SecurityUtils.validatePassword("12345")) // Too short (< 6 chars)
    }

    @Test
    fun testUsernameValidation() {
        assertNull(SecurityUtils.validateUsername("OtakuMaster"))
        assertNotNull(SecurityUtils.validateUsername(""))
        assertNotNull(SecurityUtils.validateUsername("ab")) // Too short (< 3 chars)
    }
}
