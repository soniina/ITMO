package commands;

import managers.CollectionManager;

/**
 * Команда для сохранения коллекции в файл.
 */
public class SaveCommand extends Command {
    private final CollectionManager cm;
    public SaveCommand(CollectionManager cm) {
        super("save", "сохранить коллекцию в файл");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.save();
    }
}
