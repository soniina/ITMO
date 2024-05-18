package server.utils

import common.communication.Response
import common.communication.ResponseStatus
import common.models.StudyGroup
import server.network.Server
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.time.format.DateTimeFormatter

/**
 * Утилитарный класс для работы с файлом истории команд.
 */
object HistoryWriter {

    private lateinit var server: Server
    private val historyList: MutableMap<String, ArrayList<String>> = LinkedHashMap()
    private val historyFile: MutableMap<String, String> = LinkedHashMap()
    var file: String? = null
    private var address: String? = null
    private var overflow = false

    fun init(server: Server) {
        HistoryWriter.server = server
    }

    /**
     * Записывает строку в файл history.
     * @param line строка для записи.
     */
    fun write(line: String) {
        if (!overflow) {
            if (File(file.toString()).length() / 1024 > 10) {
                overflow = true
                server.sendResponse(
                    Response(
                        ResponseStatus.HISTORY_OVERFLOW,
                        "Файл истории переполнен! Дальнейшая запись невозможна."
                    )
                )
            } else {
                historyList[address]?.add(line)
                try {
                    OutputStreamWriter(FileOutputStream(file.toString(), true)).use { outputStreamWriter ->
                        outputStreamWriter.write(line + "\n")
                        outputStreamWriter.flush()
                    }
                } catch (e: IOException) {
                    System.err.println("Ошибка при записи данных в файл " + file + ": " + e.message)
                } catch (e: SecurityException) {
                    System.err.println("Нет прав доступа к файлу " + file + ": " + e.message)
                }
            }
        }
    }

    fun writeObject(studyGroup: StudyGroup) {
        write(studyGroup.name.toString())
        val coordinates = studyGroup.coordinates
        write(coordinates?.x.toString())
        write(coordinates?.y.toString())
        write(studyGroup.studentsCount.toString())
        write(studyGroup.formOfEducation!!.form)
        val semester = studyGroup.semesterEnum
        if (semester != null) write(semester.num.toString())
        else write("")
        val groupAdmin = studyGroup.groupAdmin
        write(groupAdmin?.birthday?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString())
        write(groupAdmin?.birthday?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString())
        val eyeColor = groupAdmin?.eyeColor
        if (eyeColor != null) write(eyeColor.color)
        else write("")
        write(groupAdmin?.nationality?.country.toString())
        val adminLocation = groupAdmin?.location
        write(adminLocation?.name.toString())
        write(adminLocation?.x.toString())
        write(adminLocation?.y.toString())
        write(adminLocation?.z.toString())
    }

    /**
     * Очищает содержимое файла.
     */
    private fun clear(file: String) {
        if (historyList.containsKey(address)) historyList[address]?.clear() //??????????
        try {
            OutputStreamWriter(FileOutputStream(file)).use { outputStreamWriter ->
                outputStreamWriter.write("")
                outputStreamWriter.flush()
            }
        } catch (e: IOException) {
            System.err.println("Ошибка при очистке файла history: " + e.message)
        } catch (e: SecurityException) {
            System.err.println("Нет прав доступа к файлу history: " + e.message)
        }
    }

    fun getCommandHistory(): ArrayList<String> {
        return historyList[address]!!
    }

    private fun addClientHistory(address: String) {
        val file: String
        if (historyFile.size < 3) file = "history" + (historyFile.size + 1)
        else {
            var oldestAddress = historyFile.keys.iterator().next()
            for (addr in historyFile.keys) {
                if (File(historyFile[addr].toString()).lastModified() < File(historyFile[oldestAddress].toString()).lastModified()) {
                    oldestAddress = addr
                }
            }
            file = historyFile[oldestAddress].toString() //???????
            historyFile.remove(oldestAddress)
        }
        clear(file)
        historyFile[address] = file
        historyList[address] = ArrayList()
    }

    fun setClient(clientAddress: String) {
        if (!historyFile.containsKey(clientAddress)) addClientHistory(clientAddress)
        file = historyFile[clientAddress]
        address = clientAddress
    }
}
