package server.commands

import common.communication.Response

/**
 * Абстрактный класс, представляющий команду.
 */
abstract class Command internal constructor(val name: String, val descr: String) {
    /**
     * Выполняет команду с переданным аргументом.
     * @param arg аргумент команды.
     */
    abstract fun execute(arg: String?): Response
}
