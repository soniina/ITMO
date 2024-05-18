package server.commands

import common.communication.Response
import common.communication.ResponseStatus
import java.util.stream.Collectors

/**
 * Команда для вывода списка доступных команд с их описанием.
 */
class HelpCommand(val commands: Map<String, Command>) : Command("help", "помощь") {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return Response(
            ResponseStatus.SUCCESS, "Список доступных команд:\n" +
                    commands.values.stream().map { command: Command -> command.name + " - " + command.descr }
                        .collect(Collectors.joining("\n")) + "\n")
    }
}
