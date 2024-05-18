package common.models

import java.io.Serializable

class Location : Comparable<Location?>, Serializable {
    var x: Double? = null //Поле не может быть null
    var y: Long = 0
    var z: Float? = null //Поле не может быть null
    var name: String? = null //Поле не может быть null
        set(value) {
            if (value == null || value.trim().isEmpty()) throw NullPointerException("Location.name: Значение не может быть пустым!")
            else field = value
        }

    fun setY(line: String) {
        if (line.trim { it <= ' ' }.isEmpty()) {
            this.y = 0
            return
        }
        try {
            this.y = line.toLong()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Location.y: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    fun setX(line: String) {
        if (line.trim().isEmpty()) throw NullPointerException("Location.x: Значение не может быть пустым!")
        try {
            this.x = line.toDouble()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Location.x: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    fun setZ(line: String) {
        if (line.trim().isEmpty()) throw NullPointerException("Location.z: Значение не может быть пустым!")
        try {
            this.z = line.toFloat()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Location.z: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    override fun toString(): String {
        return "Location{" +
                "name='" + name +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z + '\'' +
                '}'
    }

    override fun compareTo(other: Location?): Int {
        if (x!!.compareTo(other!!.x!!) != 0) {
            return x!!.compareTo(other.x!!)
        } else if (y != other.y) {
            return y.compareTo(other.y)
        } else if (z!!.compareTo(other.z!!) != 0) {
            return z!!.compareTo(other.z!!)
        }
        return name!!.compareTo(other.name!!)
    }
}
