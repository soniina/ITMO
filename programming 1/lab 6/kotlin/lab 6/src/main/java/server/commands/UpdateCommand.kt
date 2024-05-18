package server.commands

import common.communication.Response
import server.managers.CollectionManager
import java.io.BufferedReader
import java.io.IOException

/**
 * Команда для обновления элемента коллекции по его идентификатору.
 */
class UpdateCommand(private val cm: CollectionManager) : Command("update id", "обновить элемент по id"), Promptable {
    /**
     * Выполняет команду.
     * @param id @param id идентификатор элемента.
     */
    override fun execute(id: String?): Response {
        requireNotNull(id) { "Команда ${name.split(' ')[0]} требует аргумент ${name.split(' ')[1]}" }
        return cm.update(id.toLong())
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param id идентификатор элемента.
     */
    @Throws(IOException::class)
    override fun execute(reader: BufferedReader, id: String?): Response {
        requireNotNull(id) { "Команда ${name.split(' ')[0]} требует аргумент ${name.split(' ')[1]}" }
        try {
            return cm.update(reader, id.toLong())
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Неверный формат аргумента! Требуется числовое значение.\n")
        }
    }
}
