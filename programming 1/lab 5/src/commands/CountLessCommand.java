package commands;

import managers.CollectionManager;

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
    public void execute(String studentsCount) {
        if (studentsCount == null) System.err.println("Команда count_less_than_students_count требует аргумент studentsCount");
        else try {
            cm.countLess(Long.parseLong(studentsCount));
        }
        catch (NumberFormatException e) {
            System.err.println("Неверный формат аргумента! Требуется числовое значение.");
        }
    }
}
