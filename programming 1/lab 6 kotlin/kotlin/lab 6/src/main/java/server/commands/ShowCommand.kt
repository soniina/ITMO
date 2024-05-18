package server.commands

import common.communication.Response
import server.managers.CollectionManager


/**
 * Команда для вывода всех элементов коллекции.
 */
class ShowCommand(private val cm: CollectionManager) : Command("show", "вывести все элементы коллекции") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.show()
    }
}
