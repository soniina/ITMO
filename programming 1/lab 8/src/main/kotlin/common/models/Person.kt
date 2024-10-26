package common.models

import client.exceptions.EnumConstantNotFoundException
import java.io.Serializable
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Person : Comparable<Person>, Serializable {
    var name: String = "" //Поле не может быть null, Строка не может быть пустой
        set(value) {
            if (value.trim().isEmpty()) throw NullPointerException("Имя админа не может быть пустым!")
            else field = value
        }
    var birthday: LocalDateTime = LocalDateTime.now() //Поле не может быть null
    var eyeColor: Color? = null //Поле может быть null
    var nationality: Country = Country.RUSSIA //Поле не может быть null
    var location: Location = Location() //Поле не может быть null

    fun setBirthday(date: String) {
        if (date.trim().isEmpty()) throw NullPointerException("День рождение админа не может быть пустым!")
        else {
            val parts = date.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            try {
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()

                this.birthday = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.MIDNIGHT)
            } catch (e: NumberFormatException) {
                throw NumberFormatException("Некорректный формат даты дня рождения админа!")
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw NumberFormatException("Некорректный формат даты дня рождения админа!")
            } catch (e: DateTimeException) {
                throw DateTimeException("Некорректная дата дня рождения админа!")
            }
        }
    }

    fun setEyeColor(color: String) {
        if (color.trim().isNotEmpty()) {
            for (value in Color.entries) {
                if (value.color == color.replace('ё', 'е').lowercase().replaceFirstChar { it.uppercase() }) {
                    this.eyeColor = value
                    return
                }
            }
            throw EnumConstantNotFoundException("Person.eyeColor: Выбранный цвет не найден!")
        }
    }

    fun setNationality(country: String) {
        if (country.trim().isEmpty()) throw NullPointerException("Person.nationality: Значение не может быть пустым!")
        for (value in Country.entries) {
            if (value.country.lowercase() == country.lowercase()) {
                this.nationality = value
                return
            }
        }
        throw EnumConstantNotFoundException("Person.nationality: Выбранная страна не найдена!")
    }

    override fun toString(): String {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", eyeColor=" + eyeColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val person = other as Person

        if (name != person.name) return false
        if (birthday != person.birthday) return false
        if (eyeColor != person.eyeColor) return false
        if (nationality != person.nationality) return false
        return location == person.location
    }

    override fun compareTo(other: Person): Int {
        if (name.compareTo(other.name) != 0) {
            return name.compareTo(other.name)
        } else if (birthday.compareTo(other.birthday) != 0) {
            return birthday.compareTo(other.birthday)
        }
        return location.compareTo(other.location)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + (eyeColor?.hashCode() ?: 0)
        result = 31 * result + nationality.hashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}
