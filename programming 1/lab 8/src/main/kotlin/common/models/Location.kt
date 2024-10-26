package common.models

import java.io.Serializable

class Location : Comparable<Location?>, Serializable {
    var x: Double = 0.0 //Поле не может быть null
    var y: Long? = null
    var z: Float = 0f //Поле не может быть null
    var name: String = "" //Поле не может быть null
        set(value) {
            if (value.trim().isEmpty()) throw NullPointerException("Название локации админа не может быть пустым!")
            else field = value
        }

    fun setY(line: String) {
        if (line.trim().isNotEmpty()) {
            try {
                this.y = line.toLong()
            } catch (e: NumberFormatException) {
                throw NumberFormatException("Location.y: Неверный формат ввода! Требуется числовое значение.")
            }
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

    fun setZ(line: String?) {
        if (line == null || line.trim().isEmpty()) throw NullPointerException("Location.z: Значение не может быть пустым!")
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val location = other as Location

        if (x != location.x) return false
        if (y != location.y) return false
        if (z != location.z) return false
        return name == location.name
    }

    override fun compareTo(other: Location?): Int {
        if (other == null) return 1
        if (x.compareTo(other.x) != 0) {
            return x.compareTo(other.x)
        } else if ((y != null) and (other.y != null) and (y != other.y)) {
            return y!!.compareTo(other.y!!)
        } else if (z.compareTo(other.z) != 0) {
            return z.compareTo(other.z)
        }
        return name.compareTo(other.name)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + (y?.hashCode() ?: 0)
        result = 31 * result + z.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
