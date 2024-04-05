package commands;

import managers.CollectionManager;

import java.io.BufferedReader;

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
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.removeGreater();
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(BufferedReader reader, String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.removeGreater(reader);
    }
}
