package commands;

import managers.CollectionManager;
import java.io.BufferedReader;

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
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.add();
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(BufferedReader reader, String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.add(reader);
    }
}
