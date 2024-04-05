package commands;

import java.io.BufferedReader;

/**
 * Интерфейс, представляющий команды, требующие ввода данных элемента.
 */
public interface Promptable {
    /**
     * Выполняет команду, используя данные из файла.
     * @param reader для чтения данных из файла.
     * @param arg аргумент команды (может быть null).
     */
    void execute(BufferedReader reader, String arg);
}
