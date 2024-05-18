package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

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
    public Response execute(String id) {
        if (id == null) throw new IllegalArgumentException("Команда remove_by_id требует аргумент id\n");
        else try {
            return cm.remove(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Неверный формат аргумента! Требуется числовое значение.\n");
        }
    }
}
