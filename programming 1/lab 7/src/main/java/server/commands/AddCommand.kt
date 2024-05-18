package server.commands

import common.communication.Response
import server.managers.CollectionManager
import java.io.BufferedReader

/**
 * Команда для добавления нового элемента в коллекцию.
 */
class AddCommand(private val cm: CollectionManager) : Command("add", "добавить новый элемент"), Promptable {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.add()
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(reader: BufferedReader, arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.add(reader)
    }
}



