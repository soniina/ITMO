package commands;

import managers.CollectionManager;

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
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else cm.printFieldAscending();
    }
}
