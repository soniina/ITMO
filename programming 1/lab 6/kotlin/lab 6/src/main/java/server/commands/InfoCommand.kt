package server.commands

import common.communication.Response
import server.managers.CollectionManager

/**
 * Команда для вывода информации о коллекции.
 */
class InfoCommand(private val cm: CollectionManager) : Command("info", "информация о коллекции") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.info()
    }
}
