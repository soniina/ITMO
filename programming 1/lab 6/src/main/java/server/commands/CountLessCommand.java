package server.commands;

import common.communication.Response;
import server.managers.CollectionManager;

/**
 * Команда для вывода количества групп, в которых число обучающихся меньше заданного.
 */
public class CountLessCommand extends Command {
    private final CollectionManager cm;

    public CountLessCommand(CollectionManager cm) {
        super("count_less_than_students_count studentsCount", "вывести кол-во групп, в которых число обучающихся меньше заданного");
        this.cm = cm;
    }

    /**
     * Выполняет команду с заданным количеством студентов.
     * @param studentsCount количество студентов.
     */
    @Override
    public Response execute(String studentsCount) {
        if (studentsCount == null) throw new IllegalArgumentException("Команда count_less_than_students_count требует аргумент studentsCount\n");
        try {
            return cm.countLess(Long.parseLong(studentsCount));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Неверный формат аргумента! Требуется числовое значение.");
        }
    }
}
