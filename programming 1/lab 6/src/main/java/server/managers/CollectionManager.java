package server.managers;

import common.communication.Response;
import common.communication.ResponseStatus;
import common.models.StudyGroup;
import common.utils.ObjectBuilder;
import server.exceptions.MissingFileException;
import server.network.Server;
import server.utils.HistoryWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Менеджер, оперирующий коллекцией
 */

public class CollectionManager {
    private final PriorityQueue<StudyGroup> collection;
    private final DumpManager dumpManager;
    private final String loadFile;
    private LocalDateTime initializationTime;
    private LocalDateTime lastSaveTime;
    private PriorityQueue<Long> availableIds = new PriorityQueue<>();
    private Set<Long> usedIds = new HashSet<>();
    private Long nextId = 1L;
    private final Server server;
    private HistoryWriter historyWriter;

    public CollectionManager(PriorityQueue<StudyGroup> collection, String loadFile, Server server) {
        this.collection = collection;
        this.loadFile = loadFile;
        dumpManager = new DumpManager(collection);
        lastSaveTime = null;
        this.server = server;
        historyWriter = new HistoryWriter(server);

        load();

        initializationTime = LocalDateTime.now();
        for (StudyGroup object : collection) {
            Long id = object.getId();
            usedIds.add(id);
            if (id.equals(nextId)) {
                nextId = nextId + 1;
            } else if (id > nextId) {
                availableIds.add(nextId);
                nextId = id + 1;
            } else {
                availableIds.add(id);
            }
        }
    }

    /**
     * Проверяет, свободен ли указанный идентификатор в коллекции.
     * @param id идентификатор для проверки.
     * @return true, если идентификатор свободен; false в противном случае.
     */
    public boolean idIsFree(Long id) {
        return !(usedIds.contains(id));
    }


    /**
     * @return свободный id.
     */
    public Long getId() {
        Long id;
        if (!availableIds.isEmpty()) {
            id = availableIds.poll();
        } else {
            id = nextId;
            nextId += 1;
            usedIds.add(id);
        }
        return id;
    }

    /**
     * Добавляет новый элемент в коллекцию.
     */
    public Response add() {
        server.sendResponse(new Response(ResponseStatus.ASK_OBJECT, "Введите данные добавляемого объекта!"));
        StudyGroup studyGroup = (StudyGroup) server.readRequest();
        historyWriter.writeObject(studyGroup);
        studyGroup.setId(String.valueOf(getId()));
        collection.add(studyGroup);
        return new Response(ResponseStatus.SUCCESS, "Элемент успешно добавлен!\n");
    }

    /**
     * Добавляет новый элемент в коллекцию на основе данных из файла.
     * @param reader для чтения данных из файла.
     */
    public Response add(BufferedReader reader) throws IOException {
        StudyGroup studyGroup = ObjectBuilder.create(reader);
        historyWriter.writeObject(studyGroup);
        studyGroup.setId(String.valueOf(getId()));
        collection.add(studyGroup);
        return new Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент успешно добавлен!\n");
    }


    /**
     * Выводит содержимое коллекции.
     */
    public Response show() {
        String descr = "";
        if (collection.isEmpty()) descr = "Коллекция пуста!";
        collection.stream().sorted().forEach(studyGroup -> server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.toString())));
        return new Response(ResponseStatus.SUCCESS, descr);
    }

    /**
     * Очищает коллекцию.
     */
    public Response clear() {
        collection.clear();
        return new Response(ResponseStatus.SUCCESS, "Коллекция пуста!");
    }

    /**
     * Выводит первый элемент коллекции.
     */
    public Response head() {
        if (collection.isEmpty()) return new Response(ResponseStatus.SUCCESS, "Коллекция пуста!");
        else return new Response(ResponseStatus.SUCCESS, collection.peek().toString() + "\n");
    }

    /**
     * Удаляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для удаления.
     */
    public Response remove(Long id) {
        if (idIsFree(id)) throw new IllegalArgumentException(String.format("Элемент с id=%s не найден!\n", id));
        availableIds.add(id);
        usedIds.remove(id);
        StudyGroup deleteGroup = collection.stream().filter(studyGroup -> studyGroup.getId().equals(id)).findFirst().get();
        collection.remove(deleteGroup);
        return new Response(ResponseStatus.SUCCESS, "Элемент успешно удалён!\n");
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для обновления.
     */
    public Response update(Long id) {
        if (idIsFree(id)) throw new IllegalArgumentException(String.format("Элемент с id=%s не найден!\n", id));
        StudyGroup deleteGroup = collection.stream().filter(studyGroup -> studyGroup.getId().equals(id)).findFirst().get();
        collection.remove(deleteGroup);

        server.sendResponse(new Response(ResponseStatus.ASK_OBJECT, "Введите обновлённые данные объекта!"));
        StudyGroup studyGroup = (StudyGroup) server.readRequest();
        historyWriter.writeObject(studyGroup);
        studyGroup.setId(String.valueOf(id));
        collection.add(studyGroup);
        return new Response(ResponseStatus.SUCCESS, "Элемент успешно обновлён!\n");
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору и берёт данные для обновления из файла.
     * @param id идентификатор элемента для удаления.
     * @param reader для чтения данных из файла.
     */
    public Response update(BufferedReader reader, Long id) throws IOException {
        if (idIsFree(id)) throw new IllegalArgumentException(String.format("Элемент с id=%s не найден!\n", id));
        collection.stream().filter(studyGroup -> studyGroup.getId().equals(id)).forEach(collection::remove);
        StudyGroup studyGroup = ObjectBuilder.create(reader);
        historyWriter.writeObject(studyGroup);
        studyGroup.setId(String.valueOf(id));
        collection.add(studyGroup);
        return new Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент c id = "  + id + " успешно обновлён!\n");
    }

    /**
     * Удаляет все элементы, превышающие заданный.
     */
    public Response removeGreater() {
        if (collection.isEmpty()) return new Response(ResponseStatus.SUCCESS, "Коллекция пуста!");
        server.sendResponse(new Response(ResponseStatus.ASK_OBJECT, "Введите данные объекта для сравнения!"));

        StudyGroup compareStudyGroup = (StudyGroup) server.readRequest();
        historyWriter.writeObject(compareStudyGroup);
        compareStudyGroup.setId("0");

        collection.stream().filter(studyGroup -> studyGroup.compareTo(compareStudyGroup) > 0).forEach(studyGroup -> {availableIds.add(studyGroup.getId()); collection.remove(studyGroup);});
        return new Response(ResponseStatus.SUCCESS, "Элемент(ы) успешно удален(ы)!");
    }

    /**
     * Удаляет все элементы, превышающие тот, чьи данные берёт из файла.
     * @param reader для чтения данных из файла.
     */
    public Response removeGreater(BufferedReader reader) throws IOException {
        if (collection.isEmpty()) return new Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Коллекция пуста!");

        StudyGroup compareStudyGroup = ObjectBuilder.create(reader);
        historyWriter.writeObject(compareStudyGroup);
        compareStudyGroup.setId("0");

        collection.stream().filter(studyGroup -> studyGroup.compareTo(compareStudyGroup) > 0).forEach(studyGroup -> {availableIds.add(studyGroup.getId()); collection.remove(studyGroup);});
        return new Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент(ы) успешно удален(ы)!");
    }

    /**
     * Выводит количество элементов, значение поля studentsCount которых меньше заданного.
     * @param studentsCount количество студентов.
     */
    public Response countLess(long studentsCount) {
        return new Response(ResponseStatus.SUCCESS, collection.stream().filter(studyGroup -> studyGroup.getStudentsCount() < studentsCount).count() + "\n");
    }

    /**
     * Выводит элементы коллекции в порядке убывания.
     */
        public Response printDescending() {
            if (collection.isEmpty()) return new Response(ResponseStatus.SUCCESS, "Коллекция пуста!");
            collection.stream().sorted(Comparator.reverseOrder()).forEach(studyGroup -> server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.toString())));
            return new Response(ResponseStatus.SUCCESS, "");
        }

    /**
     * Выводит значения поля formOfEducation всех элементов в порядке возрастания.
     */
    public Response printFieldAscending() {
        if (collection.isEmpty()) return new Response(ResponseStatus.SUCCESS, "Коллекция пуста!");
        else {
            collection.stream().sorted().forEach(studyGroup -> server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.getFormOfEducation().toString())));
            return new Response(ResponseStatus.SUCCESS, "");
        }
    }

    /**
     * Выводит информацию о коллекции.
     */

    public Response info() {
        String info =  "Тип коллекции: PriorityQueue\n" +
                "Тип элементов: StudyGroup\n" +
                "Количество элементов: " + collection.size() + "\n" +
                "Дата инициализации: " + initializationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\n";
        if (lastSaveTime == null) info += "Сохранение коллекции ещё не осуществлялось.";
        else info += "Дата последнего сохранения: " + lastSaveTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return new Response(ResponseStatus.SUCCESS, info + "\n");
    }

    public void load() {
        try {
            dumpManager.loadCollection(loadFile);
        } catch (MissingFileException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println("Файл " + loadFile + " не найден.");
        } catch (SecurityException e) {
            System.err.println("Нет прав доступа к файлу " + loadFile);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки коллекции из файла" + loadFile + ": " + e.getMessage());
        }
    }

    /**
     * Сохраняет элементы коллекции в файл, который передаётся из аргумента командной строки.
     */
    public void save() {
        try {
            dumpManager.saveCollection(loadFile);
            lastSaveTime = LocalDateTime.now();
            System.out.println("Коллекция успешно сохранена!");
        } catch (MissingFileException e) {
            System.err.println(e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Нет прав доступа к файлу " + loadFile);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения коллекция в файл" + loadFile + ": " + e.getMessage());
        }
    }
}
