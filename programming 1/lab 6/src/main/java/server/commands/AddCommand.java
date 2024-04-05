package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Команда для добавления нового элемента в коллекцию.
 */
public class AddCommand extends Command implements Promptable {

    private final CollectionManager cm;
    public AddCommand(CollectionManager cm) {
        super("add", "добавить новый элемент");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.add();
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(BufferedReader reader, String arg) throws IOException {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.add(reader);
    }
}



