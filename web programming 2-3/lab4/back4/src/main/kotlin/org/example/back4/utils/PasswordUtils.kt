package org.example.back4.utils

import java.security.MessageDigest
import java.util.Base64

object PasswordUtils {

    fun hashPassword(password: String): String {
        val hash = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }
}