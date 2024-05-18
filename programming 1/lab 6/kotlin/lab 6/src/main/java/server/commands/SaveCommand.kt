package server.commands

import common.communication.Response
import common.communication.ResponseStatus
import server.managers.CollectionManager

/**
 * Команда для сохранения коллекции в файл.
 */
class SaveCommand(private val cm: CollectionManager) : Command("save", "сохранить коллекцию в файл") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.save()
    }
}
