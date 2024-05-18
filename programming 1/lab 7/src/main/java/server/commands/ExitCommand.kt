package server.commands

import common.communication.Response
import common.communication.ResponseStatus
import server.managers.CollectionManager
import server.utils.HistoryWriter

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
        if (HistoryWriter.file != null) HistoryWriter.clear(HistoryWriter.file!!)
        return Response(ResponseStatus.EXIT, "Завершение работы...")
    }
}
