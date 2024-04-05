package managers;

import exceptions.MissingFileException;
import models.StudyGroup;
import utils.ObjectBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

    public CollectionManager(PriorityQueue<StudyGroup> collection, String loadFile) {
        this.collection = collection;
        this.loadFile = loadFile;
        this.dumpManager = new DumpManager(collection);
        this.lastSaveTime = null;

        load();
        this.initializationTime = LocalDateTime.now();
        for (StudyGroup object : collection) {
            Long id = object.getId();
            usedIds.add(id);
            if (id.equals(nextId)) {
                nextId = nextId + 1;
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
    public void add() {
        collection.add(ObjectBuilder.create(getId()));
        System.out.println("Элемент успешно добавлен!");
    }

    /**
     * Добавляет новый элемент в коллекцию на основе данных из файла.
     * @param reader для чтения данных из файла.
     */
    public void add(BufferedReader reader) {
        StudyGroup studyGroup = ObjectBuilder.create(getId(), reader);
        if (studyGroup != null) {
            collection.add(studyGroup);
            System.out.println("Элемент успешно добавлен!");
        }
    }

    /**
     * Выводит содержимое коллекции.
     */

    public void show() {
        if (collection.isEmpty()) System.out.println("Коллекция пуста!");
        else {
            PriorityQueue<StudyGroup> tempCollection = new PriorityQueue<>(collection);
            while (!tempCollection.isEmpty()) {
                System.out.println(tempCollection.poll());
            }
        }
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
        System.out.println("Коллекция пуста!");
    }

    /**
     * Выводит первый элемент коллекции.
     */
    public void head() {
        if (collection.isEmpty()) System.out.println("Коллекция пуста!");
        else System.out.println(collection.peek());
    }

    /**
     * Удаляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для удаления.
     */
    public void remove(Long id) {
        if (idIsFree(id)) {
            System.err.printf("Элемент с id=%s не найден!\n", id);
            return;
        }
        availableIds.add(id);
        for (StudyGroup studyGroup: collection) {
            if (studyGroup.getId().equals(id)) {
                collection.remove(studyGroup);
                System.out.println("Элемент успешно удалён!");
                break;
            }
        }
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для обновления.
     */
    public void update(Long id) {
        if (idIsFree(id)) {
            System.err.printf("Элемент с id=%s не найден!\n", id);
            return;
        }
        for (StudyGroup studyGroup: collection) {
            if (studyGroup.getId().equals(id)) {
                collection.remove(studyGroup);
                break;
            }
        }
        collection.add(ObjectBuilder.create(id));
        System.out.println("Элемент успешно обновлён!");
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору и берёт данные для обновления из файла.
     * @param id идентификатор элемента для удаления.
     * @param reader для чтения данных из файла.
     */
    public void update(BufferedReader reader, Long id) {
        if (idIsFree(id)) {
            System.err.printf("Элемент с id=%s не найден!\n", id);
            return;
        }
        for (StudyGroup studyGroup: collection) {
            if (studyGroup.getId().equals(id)) {
                collection.remove(studyGroup);
                break;
            }
        }
        collection.add(ObjectBuilder.create(id, reader));
        System.out.println("Элемент c id = "  + id + " успешно обновлён!");
    }

    /**
     * Удаляет все элементы, превышающие заданный.
     */
    public void removeGreater() {
        if (collection.isEmpty()) System.out.println("Коллекция пуста!");
        else {
            StudyGroup compareStudyGroup = ObjectBuilder.create(Long.valueOf(0));
            for (StudyGroup studyGroup : collection) {
                if (collection.comparator().compare(studyGroup, compareStudyGroup) > 0) {
                    availableIds.add(studyGroup.getId());
                    collection.remove(studyGroup);
                }
            }
            System.out.println("Элемент(ы) успешно удален(ы)!");
        }
    }

    /**
     * Удаляет все элементы, превышающие тот, чьи данные берёт из файла.
     * @param reader для чтения данных из файла.
     */
    public void removeGreater(BufferedReader reader) {
        if (collection.isEmpty()) System.out.println("Коллекция пуста!");
        else {
            StudyGroup compareStudyGroup = ObjectBuilder.create(Long.valueOf(0), reader);
            for (StudyGroup studyGroup : collection) {
                if (collection.comparator().compare(studyGroup, compareStudyGroup) > 0) {
                    availableIds.add(studyGroup.getId());
                    collection.remove(studyGroup);
                }
            }
            System.out.println("Элемент(ы) успешно удален(ы)!");
        }
    }

    /**
     * Выводит количество элементов, значение поля studentsCount которых меньше заданного.
     * @param studentsCount количество студентов.
     */
    public void countLess(long studentsCount) {
        int count = 0;
        for (StudyGroup studyGroup: collection) {
            if (studyGroup.getStudentsCount() < studentsCount) {
                count += 1;
            }
        }
        System.out.println(count);
    }

    /**
     * Выводит элементы коллекции в порядке убывания.
     */
    public void printDescending() {
        if (collection.isEmpty()) System.out.println("Коллекция пуста!");
        else {
            PriorityQueue<StudyGroup> tempCollection = new PriorityQueue<>(collection);

            List<StudyGroup> listCollection = new ArrayList<>();
            while (!tempCollection.isEmpty()) {
                listCollection.add(tempCollection.poll());
            }

            for (int i = listCollection.size() - 1; i >= 0; i--) {
                System.out.println(listCollection.get(i));
            }
        }
    }

    /**
     * Выводит значения поля formOfEducation всех элементов в порядке возрастания.
     */
    public void printFieldAscending() {
        if (collection.isEmpty()) System.out.println("Коллекция пуста!");
        else {
            PriorityQueue<StudyGroup> tempCollection = new PriorityQueue<>(collection);
            while (!tempCollection.isEmpty()) {
                System.out.println(tempCollection.poll().getFormOfEducation());
            }
        }
    }

    /**
     * Выводит информацию о коллекции.
     */

    public void info() {
        System.out.println("Тип коллекции: PriorityQueue");
        System.out.println("Тип элементов: StudyGroup");
        System.out.println("Количество элементов: " + collection.size());
        System.out.println("Дата инициализации: " + initializationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        if (lastSaveTime == null) System.out.println("В текущей сессии сохранение коллекции ещё не осуществлялось.");
        else System.out.println("Дата последнего сохранения: " + lastSaveTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    /**
     * Загружает элементы коллекции из файла, который передаётся из аргумента командной строки.
     */
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
