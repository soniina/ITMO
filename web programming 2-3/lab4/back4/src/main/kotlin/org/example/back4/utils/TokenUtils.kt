package org.example.back4.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

object TokenUtils {
    private val algorithm = Algorithm.HMAC256("verbasus")

    fun generateToken(login: String): String {
        return JWT.create()
            .withSubject(login)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
            .sign(algorithm)
    }

    fun validateToken(token: String): Boolean {
        return try {
            JWT.require(algorithm).build().verify(token)
            true
        } catch (e: JWTVerificationException) {
            false
        }
    }
}