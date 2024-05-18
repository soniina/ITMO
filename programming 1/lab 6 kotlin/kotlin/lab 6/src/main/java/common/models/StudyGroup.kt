package common.models

import client.exceptions.EnumConstantNotFoundException
import java.io.Serializable
import java.time.DateTimeException
import java.time.LocalDate
import java.util.*

class StudyGroup : Serializable, Comparable<StudyGroup?> {
    var id: Long? = null //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    var name: String? = null //Поле не может быть null, Строка не может быть пустой
        set(value) {
            if (value == null || value.trim().isEmpty()) throw NullPointerException("name: Значение не может быть пустым!")
            else field = value
        }
    var coordinates: Coordinates? = null //Поле не может быть null
        set(value) {
            if (value == null) throw NullPointerException("coordinates: Значение не может быть пустым!")
            else field = value
        }
    var creationDate: LocalDate? = null //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    var studentsCount: Long = 0 //Значение поля должно быть больше 0
    var formOfEducation: FormOfEducation? = null //Поле не может быть null
    var semesterEnum: Semester? = null //Поле может быть null
    var groupAdmin: Person? = null //Поле не может быть null
        set(value) {
            if (value == null) throw NullPointerException("groupAdmin: Значение не может быть пустым!")
            else field = value
        }

    fun setId(string: String?) {
        if (string == null) throw NullPointerException("id: Значение не может быть пустым!")
        try {
            val id = string.toLong()
            require(id > 0) { "id: Значение должно быть больше 0!" }
            this.id = id
        } catch (e: NumberFormatException) {
            throw NumberFormatException("id: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    fun setCreationDate(date: String) {
        if (date.trim { it <= ' ' }
                .isEmpty()) throw NullPointerException("creationDate: Значение не может быть пустым!")
        else {
            val parts = date.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            try {
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()

                this.creationDate = LocalDate.of(year, month, day)
            } catch (e: NumberFormatException) {
                throw NumberFormatException("creationDate: Некорректный формат даты!")
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw NumberFormatException("creationDate: Некорректный формат даты!")
            } catch (e: DateTimeException) {
                throw DateTimeException("creationDate: Некорректная дата!")
            }
        }
    }

    fun setStudentsCount(string: String) {
        if (string.trim { it <= ' ' }
                .isEmpty()) throw NullPointerException("studentsCount: Значение не может быть пустым!")
        try {
            val studentsCount = string.toLong()
            require(studentsCount > 0) { "studentsCount: Значение должно превышать 0!" }
            this.studentsCount = studentsCount
        } catch (e: NumberFormatException) {
            throw NumberFormatException("studentsCount: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    fun setFormOfEducation(form: String) {
        if (form.trim { it <= ' ' }
                .isEmpty()) throw NullPointerException("formOfEducation: Значение не может быть пустым!")
        for (value in FormOfEducation.entries) {
            if (value.form == form.uppercase(Locale.getDefault()) || value.name == form.uppercase(Locale.getDefault())) {
                this.formOfEducation = value
                return
            }
        }
        throw EnumConstantNotFoundException("formOfEducation: Выбранная форма обучения не найдена!")
    }

    fun setSemester(string: String) {
        if (string.trim { it <= ' ' }.isEmpty()) {
            this.semesterEnum = null
            return
        }
        try {
            val num = string.toInt()
            for (value in Semester.entries) {
                if (value.num == num) {
                    this.semesterEnum = value
                    return
                }
            }
        } catch (e: NumberFormatException) {
            for (value in Semester.entries) {
                if (value.name == string.uppercase(Locale.getDefault())) {
                    this.semesterEnum = value
                    return
                }
            }
            throw NumberFormatException("semesterEnum: Неверный формат ввода! Требуется числовое значение.")
        }
        throw EnumConstantNotFoundException("semesterEnum: Выбранный семестр не найден!")
    }

    override fun compareTo(other: StudyGroup?): Int {
        if (studentsCount != other!!.studentsCount) {
            return studentsCount.compareTo(other.studentsCount)
        } else if (semesterEnum != null && other.semesterEnum != null && semesterEnum!!.compareTo(other.semesterEnum!!) != 0) {
            return semesterEnum!!.compareTo(other.semesterEnum!!)
        } else if (formOfEducation!!.compareTo(other.formOfEducation!!) != 0) {
            return formOfEducation!!.compareTo(other.formOfEducation!!)
        }
        return id!!.compareTo(other.id!!)
    }

    override fun toString(): String {
        return "StudyGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", studentsCount=" + studentsCount +
                ", formOfEducation=" + formOfEducation +
                ", semesterEnum=" + semesterEnum +
                ", groupAdmin=" + groupAdmin +
                '}'
    }
}
