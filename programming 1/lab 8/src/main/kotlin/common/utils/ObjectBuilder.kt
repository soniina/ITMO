package common.utils

import common.models.*
import client.views.AddForm
import java.io.BufferedReader
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Утилитарный класс для создания объектов StudyGroup.
 */
object ObjectBuilder {

    fun create(addForm: AddForm): StudyGroup? {
        val studyGroup = StudyGroup()

        try {
            studyGroup.name = addForm.nameField.text
        } catch (e: NullPointerException) {
            addForm.showError(e.message!!)
            return null
        }

        val coordinates = Coordinates()
        coordinates.x = addForm.xField.value as Double
        coordinates.y = addForm.yField.value as Float

        studyGroup.coordinates = coordinates
        studyGroup.studentsCount = addForm.studentsCountField.value as Long
        studyGroup.setFormOfEducation(addForm.formOfEducationField.selectedItem!!.toString())
        if (addForm.semesterField.selectedItem == "-") studyGroup.semesterEnum = null
        else studyGroup.setSemester(addForm.semesterField.selectedItem!!.toString())

        val groupAdmin = Person()
        try {
            groupAdmin.name = addForm.adminNameField.text
        } catch (e: NullPointerException) {
            addForm.showError(e.message!!)
            return null
        }

        if (addForm.adminEyeColorField.selectedItem == "-") groupAdmin.eyeColor = null
        else groupAdmin.setEyeColor(addForm.adminEyeColorField.selectedItem!!.toString())
        groupAdmin.setNationality(addForm.adminNationalityField.selectedItem!!.toString())

        try {
            val date =
                SimpleDateFormat(addForm.adminBirthdayField.dateFormatString).format(addForm.adminBirthdayField.date)
            groupAdmin.setBirthday(date)
        } catch (e: NumberFormatException) {
            addForm.showError("Некорректный формат даты дня рождения админа!")
            return null
        } catch (e: NullPointerException) {
            addForm.showError("Некорректный формат даты дня рождения админа!")
            return null
        }

        val location = Location()
        try {
            location.name = addForm.locationNameField.text
        } catch (e: NullPointerException) {
            addForm.showError(e.message!!)
            return null
        }
        location.x = addForm.locationXField.value as Double
        location.y = addForm.locationYField.value as Long
        location.z = addForm.locationZField.value as Float
        groupAdmin.location = location
        studyGroup.groupAdmin = groupAdmin
        return studyGroup
    }
    /**
     * Создает и возвращает объект StudyGroup на основе данных из файла.
     * @param reader для чтения данных из файла.
     */
    @Throws(IOException::class)
    fun create(reader: BufferedReader): StudyGroup {
        val studyGroup = StudyGroup()

        studyGroup.name = reader.readLine()

        val coordinates = Coordinates()
        coordinates.setX(reader.readLine())
        coordinates.setY(reader.readLine())
        studyGroup.coordinates = coordinates

        studyGroup.setStudentsCount(reader.readLine())
        studyGroup.setFormOfEducation(reader.readLine())
        studyGroup.setSemester(reader.readLine())

        val groupAdmin = Person()
        groupAdmin.name = reader.readLine()
        groupAdmin.setBirthday(reader.readLine())
        groupAdmin.setEyeColor(reader.readLine())
        groupAdmin.setNationality(reader.readLine())

        val adminLocation = Location()
        adminLocation.name = reader.readLine()
        adminLocation.setX(reader.readLine())
        adminLocation.setY(reader.readLine())
        adminLocation.setZ(reader.readLine())
        groupAdmin.location = adminLocation

        studyGroup.groupAdmin = groupAdmin
        return studyGroup
    }
}

