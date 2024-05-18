package server.commands;

import common.communication.Response;
import common.communication.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда для вывода списка доступных команд с их описанием.
 */
public class HelpCommand extends Command{
    Map<String, Command> commands;
    public HelpCommand(Map<String, Command> commands) {
        super("help", "помощь");
        this.commands = commands;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return new Response(ResponseStatus.SUCCESS, "Список доступных команд:\n" +
                commands.values().stream().map(command -> command.getName() + " - " + command.getDescr())
                        .collect(Collectors.joining("\n")) + "\n");
    }
}
