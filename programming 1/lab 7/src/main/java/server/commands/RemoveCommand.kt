package server.commands

import common.communication.Response
import server.managers.CollectionManager

/**
 * Команда для удаления элемента из коллекции по его идентификатору.
 */
class RemoveCommand(private val cm: CollectionManager) : Command("remove_by_id id", "удалить элемент по id") {
    /**
     * Выполняет команду.
     * @param id идентификатор элемента.
     */
    override fun execute(id: String?): Response {
        requireNotNull(id) { "Команда ${name.split(' ')[0]} требует аргумент ${name.split(' ')[1]}" }
        try {
            return cm.remove(id.toLong())
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Неверный формат аргумента! Требуется числовое значение.\n")
        }
    }
}
