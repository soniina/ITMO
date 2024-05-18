package server.commands

import common.communication.Response
import java.io.BufferedReader
import java.io.IOException

/**
 * Интерфейс, представляющий команды, требующие ввода данных элемента.
 */
interface Promptable {
    /**
     * Выполняет команду, используя данные из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (может быть null).
     */
    fun execute(reader: BufferedReader, arg: String?): Response
}
