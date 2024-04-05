package server.commands;


import common.communication.Response;
import server.managers.CollectionManager;

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
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.show();
    }
}
