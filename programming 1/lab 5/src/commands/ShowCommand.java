package commands;

import managers.CollectionManager;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class ShowCommand extends Command {

    private final CollectionManager cm;
    public ShowCommand(CollectionManager cm) {
        super("show", "вывести все элементы коллекции");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда show не принимает аргументы\n", getName());
        else cm.show();
    }
}
