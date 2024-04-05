package commands;

import managers.CollectionManager;

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
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.head();
    }
}
