package server.utils;

import common.communication.Response;
import common.communication.ResponseStatus;
import common.models.*;
import server.network.Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Утилитарный класс для работы с файлом истории команд.
 */
public class HistoryWriter {
    final static private Map<String, List> historyList = new LinkedHashMap<>();
    final static private Map<String, String> historyFile = new LinkedHashMap<>();
    static private String file = null;
    static private String address = null;
    static private boolean overflow = false;
    private final Server server;

    public HistoryWriter(Server server) {
        this.server = server;
    }

    /**
     * Записывает строку в файл history.
     * @param line строка для записи.
     */
    public void write(String line){
        if (!overflow) {
            if (new File(file).length() / 1024 > 10) {
                overflow = true;
                server.sendResponse(new Response(ResponseStatus.HISTORY_OVERFLOW, "Файл истории переполнен! Дальнейшая запись невозможна."));
            }
            else {
                historyList.get(address).add(line);
                try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, true))) {
                    outputStreamWriter.write(line + "\n");
                    outputStreamWriter.flush();
                } catch (IOException e) {
                    System.err.println("Ошибка при записи данных в файл " + file + ": " + e.getMessage());
                } catch (SecurityException e) {
                    System.err.println("Нет прав доступа к файлу " + file + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Очищает содержимое файла.
     */
    public static void clear(String file) {
        if (historyList.containsKey(address)) historyList.get(address).clear();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file))) {
            outputStreamWriter.write("");
            outputStreamWriter.flush();
        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла history: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Нет прав доступа к файлу history: " + e.getMessage());
        }
    }

    public static List<String> getCommandHistory() {
        return historyList.get(address);
    }

    public static void addClientHistory(String address) {
        String file;
        if (historyFile.size() < 3) file = "history" + (historyFile.size() + 1);
        else {
            String oldestAddress = historyFile.keySet().iterator().next();
            for (String addr : historyFile.keySet()) {
                if (new File(historyFile.get(addr)).lastModified() < new File(historyFile.get(oldestAddress)).lastModified()) {
                    oldestAddress = addr;
                }
            }
            file = historyFile.get(oldestAddress);
            historyFile.remove(oldestAddress);
        }
        clear(file);
        historyFile.put(address, file);
        historyList.put(address, new ArrayList());
    }

    public static String getHistoryFile() {
        return file;
    }

    public static void setClient(String clientAddress) {
        if (!historyFile.containsKey(clientAddress)) addClientHistory(clientAddress);
        file = historyFile.get(clientAddress);
        address = clientAddress;
    }
    public void writeObject(StudyGroup studyGroup) {
        write(studyGroup.getName());
        Coordinates coordinates = studyGroup.getCoordinates();
        write(String.valueOf(coordinates.getX()));
        write(String.valueOf(coordinates.getY()));
        write(String.valueOf(studyGroup.getStudentsCount()));
        write(studyGroup.getFormOfEducation().getForm());
        Semester semester = studyGroup.getSemesterEnum();
        if (semester != null) write(String.valueOf(semester.getNum()));
        else write("");
        Person groupAdmin = studyGroup.getGroupAdmin();
        write(groupAdmin.getBirthday().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        write(groupAdmin.getBirthday().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        Color eyeColor = groupAdmin.getEyeColor();
        if (eyeColor != null) write(String.valueOf(eyeColor.getColor()));
        else write("");
        write(groupAdmin.getNationality().getCountry());
        Location adminLocation = groupAdmin.getLocation();
        write(adminLocation.getName());
        write(String.valueOf(adminLocation.getX()));
        write(String.valueOf(adminLocation.getY()));
        write(String.valueOf(adminLocation.getZ()));
    }
}
