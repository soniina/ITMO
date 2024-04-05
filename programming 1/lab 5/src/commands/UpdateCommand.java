package commands;

import managers.CollectionManager;

import java.io.BufferedReader;

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
    public void execute(String id) {
        if (id == null) System.err.println("Команда update требует аргумент id");
        else cm.update(Long.parseLong(id));
    }

    /**
     * Выполняет команду из файла.
     * @param reader для чтения данных из файла.
     * @param id идентификатор элемента.
     */
    @Override
    public void execute(BufferedReader reader, String id) {
        if (id == null) System.err.println("Команда update требует аргумент id");
        else try {
            cm.update(reader, Long.parseLong(id));
        }
        catch (NumberFormatException e) {
            System.err.println("Неверный формат аргумента! Требуется числовое значение.");
        }
    }
}
