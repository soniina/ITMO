package common.models

import java.io.Serializable

class Coordinates: Comparable<Coordinates>, Serializable {
    var x: Double = 0.0
    var y: Float = 0f

    fun setX(line: String) {
        if (line.trim().isEmpty()) throw NullPointerException("Coordinates.x: Значение не может быть пустым!")
        try {
            val x = line.toDouble()
            require(!(x <= -190)) { "Coordinates.x: Значение должно превышать -190!" }
            this.x = x
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Coordinates.x: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    fun setY(line: String) {
        if (line.trim().isEmpty()) throw NullPointerException("Coordinates.y: Значение не может быть пустым!")
        try {
            this.y = line.toFloat()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Coordinates.y: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    override fun toString(): String {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}'
    }

    override fun compareTo(other: Coordinates): Int {
        if (x.compareTo(other.x) != 0) {
            return x.compareTo(other.x)
        }
        return y.compareTo(other.y)
    }
}
