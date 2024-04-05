package commands;

import managers.CollectionManager;

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
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.printDescending();
    }
}
