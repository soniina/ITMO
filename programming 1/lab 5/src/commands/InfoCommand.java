package commands;

import managers.CollectionManager;

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
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.info();
    }
}
