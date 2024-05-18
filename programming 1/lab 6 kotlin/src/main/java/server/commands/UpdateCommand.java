package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Команда для обновления элемента коллекции по его идентификатору.
 */
public class UpdateCommand extends Command implements Promptable{
    private final CollectionManager cm;
    public UpdateCommand(CollectionManager cm) {
        super("update id", "обновить элемент по id");
        this.cm = cm;
    }

    /**
     * Выполняет команду.
     * @param id @param id идентификатор элемента.
     */
    @Override
    public Response execute(String id) {
        if (id == null) throw new IllegalArgumentException("Команда update требует аргумент id\n");
        return cm.update(Long.parseLong(id));
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param id идентификатор элемента.
     */
    @Override
    public Response execute(BufferedReader reader, String id) throws IOException {
        if (id == null) throw new IllegalArgumentException("Команда update требует аргумент id\n");
        try {
            return cm.update(reader, Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Неверный формат аргумента! Требуется числовое значение.\n");
        }
    }
}
