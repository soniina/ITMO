package commands;

import managers.CollectionManager;

/**
 * Команда для очистки коллекции.
 */
public class ClearCommand extends Command{
    private final CollectionManager cm;
    public ClearCommand(CollectionManager cm) {
        super("clear", "очистить коллекцию");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.clear();
    }
}
