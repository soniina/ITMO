package common.models

import java.io.Serializable
import java.time.LocalDate

class StudyGroup : Serializable, Comparable<StudyGroup?> {
    var id: Long = 0 //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    var name: String = "" //Поле не может быть null, Строка не может быть пустой
        set(value) {
            if (value.trim().isEmpty()) throw NullPointerException("Название не может быть пустым!")
            else field = value
        }
    var coordinates: Coordinates = Coordinates()//Поле не может быть null
    var creationDate: LocalDate = LocalDate.now() //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    var studentsCount: Long = 0 //Значение поля должно быть больше 0
    var formOfEducation: FormOfEducation = FormOfEducation.FULL_TIME_EDUCATION //Поле не может быть null
    var semesterEnum: Semester? = null //Поле может быть null
    var groupAdmin: Person = Person()//Поле не может быть null

    fun setStudentsCount(string: String) {
        if (string.trim().isEmpty()) throw NullPointerException("studentsCount: Значение не может быть пустым!")
        try {
            val studentsCount = string.toLong()
            require(studentsCount > 0) { "studentsCount: Значение должно превышать 0!" }
            this.studentsCount = studentsCount
        } catch (e: NumberFormatException) {
            throw NumberFormatException("studentsCount: Неверный формат ввода! Требуется числовое значение.")
        }
    }

    fun setFormOfEducation(form: String) {
//        if (form.trim().isEmpty()) throw NullPointerException("formOfEducation: Значение не может быть пустым!")
        for (value in FormOfEducation.entries) {
            if (value.form == form.lowercase().replaceFirstChar { it.uppercase() }) {
                this.formOfEducation = value
            }
        }
//        throw EnumConstantNotFoundException("formOfEducation: Выбранная форма обучения не найдена!")
    }

    fun setSemester(string: String) {
        for (value in Semester.entries) {
            if (value.name == string.lowercase().replaceFirstChar { it.uppercase() }) {
                this.semesterEnum = value
            }
        }
    }

    override fun compareTo(other: StudyGroup?): Int {
        if (other == null) return 0
        return id.compareTo(other.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as StudyGroup
        if (id != that.id) return false
        if (studentsCount != that.studentsCount) return false
        if (name != that.name) return false
        if (coordinates != that.coordinates) return false
        if (creationDate != that.creationDate) return false
        if (formOfEducation != that.formOfEducation) return false
        if (semesterEnum != that.semesterEnum) return false
        return groupAdmin == that.groupAdmin
    }

    override fun toString(): String {
        return "StudyGroup(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", studentsCount=" + studentsCount +
                ", formOfEducation=" + formOfEducation +
                ", semesterEnum=" + semesterEnum +
                ", groupAdmin=" + groupAdmin +
                ')'
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + coordinates.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + studentsCount.hashCode()
        result = 31 * result + formOfEducation.hashCode()
        result = 31 * result + (semesterEnum?.hashCode() ?: 0)
        result = 31 * result + groupAdmin.hashCode()
        return result
    }
}
