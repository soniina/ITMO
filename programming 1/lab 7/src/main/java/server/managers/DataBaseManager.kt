package server.managers

import common.models.*
import java.io.FileInputStream
import java.sql.*
import java.time.ZoneOffset
import java.util.*

class DataBaseManager {
    var userId = ThreadLocal<Long>()
    val connection: Connection

    init {
        val info = Properties()
        info.load(FileInputStream("db.cfg"))
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", info)
        } catch (e: SQLException) {
            throw RuntimeException(e.message)
        }
    }

    fun load(): PriorityQueue<StudyGroup> {
        val collection = PriorityQueue<StudyGroup>()
        try {
            val resultStudyGroups = connection.prepareStatement("SELECT * FROM study_groups;").executeQuery()
            while (resultStudyGroups.next()) {
                val studyGroup = StudyGroup()
                studyGroup.id = resultStudyGroups.getLong("id")
                studyGroup.name = resultStudyGroups.getString("name")

                val coordinates = Coordinates()
                val getCoordinates = connection.prepareStatement("SELECT * FROM coordinates WHERE id = (?);")
                getCoordinates.setLong(1, resultStudyGroups.getLong("coordinates"))
                val resultCoordinates = getCoordinates.executeQuery()
                if (resultCoordinates.next()) {
                    coordinates.x = resultCoordinates.getDouble("x")
                    coordinates.y = resultCoordinates.getFloat("y")
                } else throw SQLException()

                resultCoordinates.close()
                getCoordinates.close()

                studyGroup.coordinates = coordinates
                studyGroup.studentsCount = resultStudyGroups.getLong("students_count")
                studyGroup.formOfEducation = FormOfEducation.valueOf(resultStudyGroups.getString("form_of_education"))
                val semester = resultStudyGroups.getString("semester_enum")
                studyGroup.semesterEnum = if (resultStudyGroups.wasNull()) null else Semester.valueOf(semester)

                val admin = Person()
                val getAdmin = connection.prepareStatement("SELECT * FROM persons WHERE id = (?);")
                getAdmin.setLong(1, resultStudyGroups.getLong("group_admin"))
                val resultAdmin = getAdmin.executeQuery()


                if (resultAdmin.next()) {
                    admin.name = resultAdmin.getString("name")
                    admin.birthday = resultAdmin.getTimestamp("birthday").toLocalDateTime()
                    val eyeColor = resultAdmin.getString("eye_color")
                    admin.eyeColor = if (resultAdmin.wasNull()) null else Color.valueOf(eyeColor)
                    admin.nationality = Country.valueOf(resultAdmin.getString("nationality"))


                    val adminLocation = Location()
                    val getAdminLocation = connection.prepareStatement("SELECT * FROM locations WHERE id = (?);")
                    getAdminLocation.setLong(1, resultAdmin.getLong("location"))
                    val resultAdminLocation = getAdminLocation.executeQuery()
                    if (resultAdminLocation.next()) {
                        adminLocation.x = resultAdminLocation.getDouble("x")
                        val y = resultAdminLocation.getLong("y")
                        adminLocation.y = if (resultAdminLocation.wasNull()) null else y
                        adminLocation.z = resultAdminLocation.getFloat("z")
                        adminLocation.name = resultAdminLocation.getString("name")

                        admin.location = adminLocation
                    } else throw SQLException()

                    resultAdminLocation.close()
                    getAdminLocation.close()

                } else throw SQLException()

                resultAdmin.close()
                getAdmin.close()

                studyGroup.groupAdmin = admin

                collection.add(studyGroup)
            }
            resultStudyGroups.close()
        } catch (e: SQLException) {
            println(e.message)
        }
        return collection
    }

    fun add(studyGroup: StudyGroup): Long {
        try {
            val insertStudyGroup =
                connection.prepareStatement("INSERT INTO study_groups (name, coordinates, students_count, form_of_education, semester_enum, group_admin, created_by) VALUES (?, ?, ?, ?::form_of_education, ?::semester, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)
            insertStudyGroup.setString(1, studyGroup.name)

            val insertCoordinates = connection.prepareStatement("INSERT INTO coordinates (x, y) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS)
            insertCoordinates.setDouble(1, studyGroup.coordinates.x)
            insertCoordinates.setFloat(2, studyGroup.coordinates.y)
            insertCoordinates.executeUpdate()

            val coordinatesKeys = insertCoordinates.generatedKeys
            if (coordinatesKeys.next()) insertStudyGroup.setLong(2, coordinatesKeys.getLong(1))
            else throw SQLException()
            coordinatesKeys.close()

            insertCoordinates.close()

            insertStudyGroup.setLong(3, studyGroup.studentsCount)
            insertStudyGroup.setString(4, studyGroup.formOfEducation.name)
            if (studyGroup.semesterEnum != null) {
                insertStudyGroup.setString(5, studyGroup.semesterEnum?.name)
            } else {
                insertStudyGroup.setNull(5, Types.OTHER)
            }

            val insertPerson =
                connection.prepareStatement("INSERT INTO persons (name, birthday, eye_color, nationality, location) VALUES (?, ?, ?::color, ?::country, ?);",
                    Statement.RETURN_GENERATED_KEYS)
            insertPerson.setString(1, studyGroup.groupAdmin.name)
            insertPerson.setTimestamp(2, Timestamp(studyGroup.groupAdmin.birthday.toEpochSecond(ZoneOffset.UTC) * 1000))
            if (studyGroup.groupAdmin.eyeColor != null) {
                insertPerson.setString(3, studyGroup.groupAdmin.eyeColor?.name)
            } else insertPerson.setNull(3, Types.OTHER)
            insertPerson.setString(4, studyGroup.groupAdmin.nationality.name)

            val insertLocation =
                connection.prepareStatement("INSERT INTO locations (x, y, z, name) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)
            insertLocation.setDouble(1, studyGroup.groupAdmin.location.x)
            if (studyGroup.groupAdmin.location.y != null) {
                insertLocation.setLong(2, studyGroup.groupAdmin.location.y!!)
            } else insertLocation.setNull(2, Types.OTHER)
            insertLocation.setFloat(3, studyGroup.groupAdmin.location.z)
            insertLocation.setString(4, studyGroup.groupAdmin.location.name)
            insertLocation.executeUpdate()

            val locationKeys = insertLocation.generatedKeys
            if (locationKeys.next()) insertPerson.setLong(5, locationKeys.getLong(1))
            else throw SQLException()
            locationKeys.close()

            insertLocation.close()

            insertPerson.executeUpdate()

            val personKeys = insertPerson.generatedKeys
            if (personKeys.next()) insertStudyGroup.setLong(6, personKeys.getLong(1))
            else throw SQLException()
            personKeys.close()

            insertPerson.close()

            insertStudyGroup.setLong(7, userId.get())
            insertStudyGroup.executeUpdate()

            val studyGroupKeys = insertStudyGroup.generatedKeys
            var id = 0L
            if (studyGroupKeys.next()) id = studyGroupKeys.getLong(1)
            studyGroupKeys.close()
            insertStudyGroup.close()
            if (id == 0L) throw SQLException()
            return id
        } catch (e: SQLException) {
            println(e.message)
            return 0
        }
    }

    fun clear() {
        connection.prepareStatement("DELETE FROM study_groups;").executeUpdate()
        connection.prepareStatement("DELETE FROM coordinates;").executeUpdate()
        connection.prepareStatement("DELETE FROM persons;").executeUpdate()
        connection.prepareStatement("DELETE FROM locations;").executeUpdate()
    }

    fun remove(id: Long): Boolean {
        try {
            val getIds =
                connection.prepareStatement("SELECT coordinates, group_admin FROM study_groups WHERE id = (?);")
            getIds.setLong(1, id)
            val resultIds = getIds.executeQuery()
            val coordinatesId: Long
            val groupAdminId: Long
            if (resultIds.next()) {
                coordinatesId = resultIds.getLong("coordinates")
                groupAdminId = resultIds.getLong("group_admin")
            } else throw SQLException()
            resultIds.close()
            getIds.close()

            val getLocationId = connection.prepareStatement("SELECT location FROM persons WHERE id = (?);")
            getLocationId.setLong(1, groupAdminId)
            val resultLocationId = getLocationId.executeQuery()
            val locationId: Long
            if (resultLocationId.next()) locationId = resultLocationId.getLong("location")
            else throw SQLException()
            resultLocationId.close()

            getLocationId.close()

            val deleteStudyGroup = connection.prepareStatement("DELETE FROM study_groups WHERE id = (?);")
            deleteStudyGroup.setLong(1, id)
            deleteStudyGroup.executeUpdate()

            deleteStudyGroup.close()

            val deleteCoordinates = connection.prepareStatement("DELETE FROM coordinates WHERE id = (?);")
            deleteCoordinates.setLong(1, coordinatesId)
            deleteCoordinates.executeUpdate()

            deleteCoordinates.close()

            val deleteGroupAdmin = connection.prepareStatement("DELETE FROM persons WHERE id = (?);")
            deleteGroupAdmin.setLong(1, groupAdminId)
            deleteGroupAdmin.executeUpdate()

            deleteGroupAdmin.close()

            val deleteLocation = connection.prepareStatement("DELETE FROM locations WHERE id = (?);")
            deleteLocation.setLong(1, locationId)
            deleteLocation.executeUpdate()

            deleteLocation.close()
            return true
        } catch (e: SQLException) {
            println(e.message)
            return false }
    }

    fun update(id: Long, studyGroup: StudyGroup): Boolean {
        try {
            val getIds =
                connection.prepareStatement("SELECT coordinates, group_admin FROM study_groups WHERE id = (?);")
            getIds.setLong(1, id)
            val resultIds = getIds.executeQuery()
            val coordinatesId: Long
            val groupAdminId: Long
            if (resultIds.next()) {
                coordinatesId = resultIds.getLong("coordinates")
                groupAdminId = resultIds.getLong("group_admin")
            } else throw SQLException()
            resultIds.close()

            getIds.close()

            val updateCoordinates = connection.prepareStatement(
                """
               UPDATE coordinates
               SET x = ?, y = ?
               WHERE id = (?);
            """.trimIndent()
            )
            updateCoordinates.setDouble(1, studyGroup.coordinates.x)
            updateCoordinates.setFloat(2, studyGroup.coordinates.y)
            updateCoordinates.setLong(3, coordinatesId)
            updateCoordinates.executeUpdate()

            updateCoordinates.close()

            val getLocationId = connection.prepareStatement("SELECT location FROM persons WHERE id = (?);")
            getLocationId.setLong(1, groupAdminId)
            val resultLocationId = getLocationId.executeQuery()
            val locationId: Long
            if (resultLocationId.next()) locationId = resultLocationId.getLong("location")
            else throw SQLException()
            resultLocationId.close()

            getLocationId.close()

            val updateLocation = connection.prepareStatement(
                """
                    UPDATE locations
                    SET x = ?, y = ?, z = ?, name = ?
                    WHERE id = (?);
                """.trimIndent()
            )
            updateLocation.setDouble(1, studyGroup.groupAdmin.location.x)
            if (studyGroup.groupAdmin.location.y != null) {
                updateLocation.setLong(2, studyGroup.groupAdmin.location.y!!)
            } else {
                updateLocation.setNull(2, Types.OTHER)
            }
            updateLocation.setFloat(3, studyGroup.groupAdmin.location.z)
            updateLocation.setString(4, studyGroup.groupAdmin.location.name)
            updateLocation.setLong(5, locationId)
            updateLocation.executeUpdate()

            updateLocation.close()

            val updateGroupAdmin = connection.prepareStatement(
                """
                    UPDATE persons
                    SET name = ?, 
                    birthday = ?,
                    eye_color = ?::color,
                    nationality = ?::country
                    WHERE id = ?;
                """.trimIndent()
            )
            updateGroupAdmin.setString(1, studyGroup.groupAdmin.name)
            updateGroupAdmin.setTimestamp(
                2,
                Timestamp(studyGroup.groupAdmin.birthday.toEpochSecond(ZoneOffset.UTC) * 1000)
            )
            if (studyGroup.groupAdmin.eyeColor != null) {
                updateGroupAdmin.setString(3, studyGroup.groupAdmin.eyeColor?.name)
            } else updateGroupAdmin.setNull(3, Types.OTHER)
            updateGroupAdmin.setString(4, studyGroup.groupAdmin.nationality.name)
            updateGroupAdmin.setLong(5, groupAdminId)
            updateGroupAdmin.executeUpdate()

            updateGroupAdmin.close()

            val updateStudyGroup = connection.prepareStatement(
                """
                    UPDATE study_groups
                    SET name = ?,
                        students_count = ?,
                        form_of_education = ?::form_of_education,
                        semester_enum = ?::semester
                    WHERE id = ?;
                """.trimIndent()
            )
            updateStudyGroup.setString(1, studyGroup.name)
            updateStudyGroup.setLong(2, studyGroup.studentsCount)
            updateStudyGroup.setString(3, studyGroup.formOfEducation.name)
            if (studyGroup.semesterEnum != null) {
                updateStudyGroup.setString(4, studyGroup.semesterEnum!!.name)
            } else updateStudyGroup.setNull(4, Types.OTHER)
            updateStudyGroup.setLong(5, id)
            updateStudyGroup.executeUpdate()

            updateStudyGroup.close()
            return true
        } catch (e: SQLException) {
            return false
        }
    }

    fun exist(id: Long): Boolean {
        val statement = connection.prepareStatement("SELECT EXISTS (SELECT id FROM study_groups WHERE id = ? );")
        statement.setLong(1, id)
        var result: ResultSet? = null
        try {
            result = statement.executeQuery()
            result.next()
            return result.getBoolean(1)
        } finally {
            statement.close()
            result?.close()
        }
    }

    fun checkCreatorId(objectId: Long): Boolean {
        val statement = connection.prepareStatement( "SELECT created_by FROM study_groups WHERE id = ?;")
        statement.setLong(1, objectId)
        var result: ResultSet? = null
        try {
            result = statement.executeQuery()
            result.next()
            return userId.get() == result.getLong("created_by")
        } finally {
            statement.close()
            result?.close()
        }
    }

    fun countUsersObjects(): Int {
        val statement = connection.prepareStatement("SELECT COUNT(*) FROM study_groups WHERE created_by = ?")
        statement.setLong(1, userId.get())
        var result : ResultSet? = null
        try {
            result = statement.executeQuery()
            result.next()
            return result.getInt(1)
        } finally {
            statement.close()
            result?.close()
        }
    }
}



