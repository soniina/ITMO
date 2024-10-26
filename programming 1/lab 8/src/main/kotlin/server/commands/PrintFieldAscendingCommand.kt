package server.commands

import common.communication.Response
import server.managers.CollectionManager


/**
 * Команда для вывода значений поля formOfEducation всех элементов коллекции в порядке возрастания.
 */
class PrintFieldAscendingCommand(private val cm: CollectionManager) : Command(
    "print_field_ascending_form_of_education",
    " вывести значения поля formOfEducation всех элементов в порядке возрастания"
) {
    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    override fun execute(arg: String?): Response {
        require(arg == null) { "Команда $name не принимает аргументы" }
        return cm.printFieldAscending()
    }
}
