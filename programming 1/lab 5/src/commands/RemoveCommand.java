package commands;

import managers.CollectionManager;

/**
 * Команда для удаления элемента из коллекции по его идентификатору.
 */
public class RemoveCommand extends Command {
    private final CollectionManager cm;
    public RemoveCommand(CollectionManager cm) {
        super("remove_by_id id", "удалить элемент по id");
        this.cm = cm;
    }

    /**
     * Выполняет команду.
     * @param id идентификатор элемента.
     */
    @Override
    public void execute(String id) {
        if (id == null) System.err.println("Команда remove_by_id требует аргумент id");
        else try {
            cm.remove(Long.parseLong(id));
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат аргумента! Требуется числовое значение.");
        }
    }
}
