package server.commands;


import common.communication.Response;
import server.managers.CollectionManager;

/**
 * Команда для вывода значений поля formOfEducation всех элементов коллекции в порядке возрастания.
 */
public class PrintFieldAscendingCommand extends Command{
    private final CollectionManager cm;

    public PrintFieldAscendingCommand(CollectionManager cm) {
        super("print_field_ascending_form_of_education", " вывести значения поля formOfEducation всех элементов в порядке возрастания");
        this.cm = cm;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        return cm.printFieldAscending();
    }
}
