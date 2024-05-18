package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

/**
 * Команда для вывода информации о коллекции.
 */
public class InfoCommand extends Command {
    private final CollectionManager cm;
    public InfoCommand(CollectionManager cm) {
        super("info", "информация о коллекции");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.info();
    }
}
