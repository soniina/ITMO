package org.example.back4.entity

import jakarta.persistence.Table

@Table(name = "users")
data class User(
    val username: String,

    val password: String
)
