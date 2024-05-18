package server.commands;

import common.communication.Response;
import common.communication.ResponseStatus;
import server.managers.CollectionManager;

/**
 * Команда для завершения работы приложения.
 */
public class ExitCommand extends Command{
    private final CollectionManager cm;
    public ExitCommand (CollectionManager cm) {
        super("exit", "завершить работу");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        else {
            cm.save();
            return new Response(ResponseStatus.EXIT, "Завершение работы...");
        }
    }
}
