package common.utils

import common.models.*
import client.exceptions.EnumConstantNotFoundException
import java.io.BufferedReader
import java.io.IOException
import java.time.DateTimeException
import java.util.*

/**
 * Утилитарный класс для создания объектов StudyGroup.
 */
object ObjectBuilder {
    /**
     * Запрашивает ввод пользователя с заданным приглашением и возвращает введенную строку.
     * @param prompt приглашение для ввода данных.
     */
    private fun prompt(prompt: String): String {
        println(prompt)
        return Scanner(System.`in`).nextLine()
    }

    /**
     * Создает и возвращает объект StudyGroup на основе ввода пользователя.
     */
    fun create(): StudyGroup {
        val studyGroup = StudyGroup()

        while (true) {
            try {
                studyGroup.name = prompt("Введите название группы:")
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }


        val coordinates = Coordinates()
        while (true) {
            try {
                coordinates.setX(prompt("Введите координату x:"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: IllegalArgumentException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                coordinates.setY(prompt("Введите координату y:"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: NumberFormatException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }
        studyGroup.coordinates = coordinates

        while (true) {
            try {
                studyGroup.setStudentsCount(prompt("Введите число студентов:"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: IllegalArgumentException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                studyGroup.setFormOfEducation(prompt("Введите форму обучения. Доступны: дистанционная, очная, вечерняя"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: EnumConstantNotFoundException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                studyGroup.setSemester(prompt("Введите семестр. Доступны: 1, 3, 7, 8"))
                break
            } catch (e: NumberFormatException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: EnumConstantNotFoundException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        println("Введите данные админа группы.")
        val groupAdmin = Person()

        while (true) {
            try {
                groupAdmin.name = prompt("Введите его имя:")
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                groupAdmin.setBirthday(prompt("Введите его день рождения в формате dd.MM.yyyy:"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: NumberFormatException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: DateTimeException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                groupAdmin.setEyeColor(prompt("Введите его цвет глаз. Доступны: синий, жёлтый, оранжевый, белый"))
                break
            } catch (e: EnumConstantNotFoundException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                groupAdmin.setNationality(prompt("Введите его страну происхождения. Доступны: Россия, Великобритания, США, Северная Корея"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: EnumConstantNotFoundException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        println("Введите его местоположение.")
        val adminLocation = Location()

        while (true) {
            try {
                adminLocation.name = prompt("Введите название локации:")
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                adminLocation.setX(prompt("Введите координату x:"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: NumberFormatException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                adminLocation.setY(prompt("Введите координату y:"))
                break
            } catch (e: NumberFormatException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        while (true) {
            try {
                adminLocation.setZ(prompt("Введите координату z:"))
                break
            } catch (e: NullPointerException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            } catch (e: NumberFormatException) {
                System.err.println(e.message + " Попробуйте ещё раз.")
            }
        }

        groupAdmin.location = adminLocation
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


