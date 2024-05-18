package server.commands

import common.communication.Response
import server.managers.CollectionManager


/**
 * Команда для очистки коллекции.
 */
class ClearCommand(private val cm: CollectionManager) : Command("clear", "очистить коллекцию") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.clear()
    }
}
