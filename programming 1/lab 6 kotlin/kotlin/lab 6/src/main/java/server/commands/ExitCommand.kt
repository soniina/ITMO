package server.commands

import common.communication.Response
import common.communication.ResponseStatus
import server.managers.CollectionManager

/**
 * Команда для завершения работы приложения.
 */
class ExitCommand(private val cm: CollectionManager) : Command("exit", "завершить работу") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        cm.save()
        return Response(ResponseStatus.EXIT, "Завершение работы...")
    }
}
