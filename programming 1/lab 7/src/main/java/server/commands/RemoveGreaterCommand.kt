package server.commands

import common.communication.Response
import server.managers.CollectionManager
import java.io.BufferedReader

/**
 * Команда для удаления элементов из коллекции, которые превышают заданный.
 */
class RemoveGreaterCommand(private val cm: CollectionManager) :
    Command("remove_greater", "удалить элементы, превышающие заданный"), Promptable {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.removeGreater()
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(reader: BufferedReader, arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.removeGreater(reader)
    }
}
