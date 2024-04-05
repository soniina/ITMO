package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Утилитарный класс для работы с файлом истории команд.
 */
public class HistoryWriter {

    /**
     * Записывает строку в файл history.
     * @param line строка для записи.
     */
    public void write(String line) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream("history", true))) {
            outputStreamWriter.write(line + "\n");
            outputStreamWriter.flush();
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в файл history: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Нет прав доступа к файлу history: " + e.getMessage());
        }
    }

    /**
     * Очищает содержимое файла history.
     */
    public void clear() {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream("history"))) {
            outputStreamWriter.write("");
            outputStreamWriter.flush();
        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла history: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Нет прав доступа к файлу history: " + e.getMessage());
        }
    }
}
