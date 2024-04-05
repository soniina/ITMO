package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Команда для удаления элементов из коллекции, которые превышают заданный.
 */
public class RemoveGreaterCommand extends Command implements Promptable {

    private final CollectionManager cm;
    public RemoveGreaterCommand(CollectionManager cm) {
        super("remove_greater", "удалить элементы, превышающие заданный");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.removeGreater();
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(BufferedReader reader, String arg) throws IOException {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.removeGreater(reader);
    }
}
