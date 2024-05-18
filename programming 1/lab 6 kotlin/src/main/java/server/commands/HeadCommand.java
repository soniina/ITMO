package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

/**
 * Команда для вывода первого элемента коллекции.
 */
public class HeadCommand extends Command{
    private final CollectionManager cm;
    public HeadCommand(CollectionManager cm) {
        super("head", "вывести первый элемент коллекции");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.head();
    }
}
