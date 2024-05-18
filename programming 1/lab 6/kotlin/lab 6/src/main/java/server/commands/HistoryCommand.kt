package server.commands

import common.communication.Response
import common.communication.ResponseStatus
import server.utils.HistoryWriter
import java.util.stream.Collectors
import kotlin.math.max

/**
 * Команда для вывода истории команд.
 */
class HistoryCommand : Command("history", "последние команды") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        val history: List<String> = HistoryWriter.getCommandHistory()
        return Response(ResponseStatus.SUCCESS,
            "${history.stream().skip(max(0.0, (history.size - 14).toDouble()).toLong())
                    .collect(Collectors.joining("\n"))} \n\n " +
                    "*все команды находятся в файле ${HistoryWriter.file}"
        )
    }
}
