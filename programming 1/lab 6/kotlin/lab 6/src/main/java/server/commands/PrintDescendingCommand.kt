package server.commands

import common.communication.Response
import server.managers.CollectionManager

/**
 * Команда для вывода элементов коллекции в порядке убывания.
 */
class PrintDescendingCommand(private val cm: CollectionManager) :
    Command("print_descending", "вывести элементы в порядке убывания") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.printDescending()
    }
}
