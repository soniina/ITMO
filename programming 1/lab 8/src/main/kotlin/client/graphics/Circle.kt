package client.graphics

import java.awt.Color
import kotlin.math.*

data class Circle(val x: Double, val y: Float, var diameter: Long, val color: Color, val text: String, val info: String) {

    fun contains(mouseX: Int, mouseY: Int): Boolean {
        val circleDiameter = max(min(diameter, 250), 10)
        return sqrt((x - mouseX).pow(2) + (y - mouseY).pow(2)) <= circleDiameter / 2
    }
}
