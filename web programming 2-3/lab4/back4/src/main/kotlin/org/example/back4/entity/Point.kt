package org.example.back4.entity

import jakarta.persistence.Table
import java.util.*

@Table(name = "points")
data class Point(
    val id: UUID = UUID.randomUUID(),

    val x: Double,

    val y: Double,

    val r: Int,

    val inArea: Boolean
)
