package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

/**
 * Команда для вывода элементов коллекции в порядке убывания.
 */
public class PrintDescendingCommand extends Command {
    private final CollectionManager cm;
    public PrintDescendingCommand(CollectionManager cm) {
        super("print_descending", "вывести элементы в порядке убывания");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.printDescending();
    }
}
