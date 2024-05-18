package server.commands

import common.communication.Response
import server.managers.CollectionManager

/**
 * Команда для вывода первого элемента коллекции.
 */
class HeadCommand(private val cm: CollectionManager) : Command("head", "вывести первый элемент коллекции") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.head()
    }
}
