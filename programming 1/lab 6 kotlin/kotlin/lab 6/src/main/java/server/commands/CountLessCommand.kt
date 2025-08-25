package server.commands

import common.communication.Response
import server.managers.CollectionManager

/**
 * Команда для вывода количества групп, в которых число обучающихся меньше заданного.
 */
class CountLessCommand(private val cm: CollectionManager) : Command(
    "count_less_than_students_count studentsCount",
    "вывести кол-во групп, в которых число обучающихся меньше заданного"
) {
    /**
     * Выполняет команду с заданным количеством студентов.
     * @param arg количество студентов.
     */
    override fun execute(arg: String?): Response {
        requireNotNull(arg) { "Команда ${name.split(' ')[0]} требует аргумент ${name.split(' ')[1]}" }
        try {
            return cm.countLess(arg.toLong())
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Неверный формат аргумента! Требуется числовое значение.")
        }
    }
}
