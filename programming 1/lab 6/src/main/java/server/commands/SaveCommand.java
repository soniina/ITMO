package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

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
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        else cm.save();
        return null;
    }
}
