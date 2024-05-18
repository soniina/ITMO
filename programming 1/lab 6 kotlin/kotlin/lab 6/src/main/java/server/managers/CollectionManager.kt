package server.managers

import common.communication.Response
import common.communication.ResponseStatus
import common.models.StudyGroup
import common.utils.ObjectBuilder
import server.exceptions.MissingFileException
import server.network.Server
import server.utils.HistoryWriter
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Менеджер, оперирующий коллекцией
 */
class CollectionManager(
    private val collection: PriorityQueue<StudyGroup>,
    private val loadFile: String?,
    private val server: Server
) {
    private val dumpManager = DumpManager(collection)
    private val initializationTime: LocalDateTime
    private var lastSaveTime: LocalDateTime? = null
    private val availableIds = PriorityQueue<Long>()
    private val usedIds: MutableSet<Long> = HashSet()
    private var nextId = 1L

    init {
        load()
        initializationTime = LocalDateTime.now()
        for (studyGroup in collection) {
            val id = studyGroup.id
            usedIds.add(id!!)
            if (id == nextId) {
                nextId += 1
            } else if (id > nextId) {
                availableIds.add(nextId)
                nextId = id + 1
            } else {
                availableIds.add(id)
            }
        }
    }

    /**
     * Проверяет, свободен ли указанный идентификатор в коллекции.
     * @param id идентификатор для проверки.
     * @return true, если идентификатор свободен; false в противном случае.
     */
    private fun idIsFree(id: Long): Boolean {
        return !(usedIds.contains(id))
    }


    private val id: Long
        /**
         * @return свободный id.
         */
        get() {
            val id: Long
            if (!availableIds.isEmpty()) {
                id = availableIds.poll()
            } else {
                id = nextId
                nextId += 1
                usedIds.add(id)
            }
            return id
        }

    /**
     * Добавляет новый элемент в коллекцию.
     */
    fun add(): Response {
        server.sendResponse(Response(ResponseStatus.ASK_OBJECT, "Введите данные добавляемого объекта!"))
        val studyGroup = server.readRequest() as StudyGroup
        HistoryWriter.writeObject(studyGroup)
        studyGroup.setId(id.toString())
        collection.add(studyGroup)
        return Response(ResponseStatus.SUCCESS, "Элемент успешно добавлен!\n")
    }

    /**
     * Добавляет новый элемент в коллекцию на основе данных из файла.
     * @param reader для чтения данных из файла.
     */
    fun add(reader: BufferedReader): Response {
        val studyGroup = ObjectBuilder.create(reader)
        HistoryWriter.writeObject(studyGroup)
        studyGroup.setId(id.toString())
        collection.add(studyGroup)
        return Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент успешно добавлен!\n")
    }


    /**
     * Выводит содержимое коллекции.
     */
    fun show(): Response {
        var descr = ""
        if (collection.isEmpty()) descr = "Коллекция пуста!"
        collection.stream().sorted().forEach { studyGroup: StudyGroup ->
            server.sendResponse(
                Response(
                    ResponseStatus.INTERMEDIATE_SUCCESS,
                    studyGroup.toString()
                )
            )
        }
        return Response(ResponseStatus.SUCCESS, descr)
    }

    /**
     * Очищает коллекцию.
     */
    fun clear(): Response {
        collection.clear()
        return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
    }

    /**
     * Выводит первый элемент коллекции.
     */
    fun head(): Response {
        return if (collection.isEmpty()) Response(
            ResponseStatus.SUCCESS,
            "Коллекция пуста!"
        )
        else Response(ResponseStatus.SUCCESS, collection.peek().toString() + "\n")
    }

    /**
     * Удаляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для удаления.
     */
    fun remove(id: Long): Response {
        require(!idIsFree(id)) { "Элемент с id=$id не найден!\n" }
        availableIds.add(id)
        usedIds.remove(id)
        val deleteGroup =
            collection.stream().filter { studyGroup: StudyGroup -> studyGroup.id == id }.findFirst().get()
        collection.remove(deleteGroup)
        return Response(ResponseStatus.SUCCESS, "Элемент успешно удалён!\n")
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для обновления.
     */
    fun update(id: Long): Response {
        require(!idIsFree(id)) { "Элемент с id=$id не найден!\n" }
        val deleteGroup =
            collection.stream().filter { studyGroup: StudyGroup -> studyGroup.id == id}.findFirst().get()
        collection.remove(deleteGroup)

        server.sendResponse(Response(ResponseStatus.ASK_OBJECT, "Введите обновлённые данные объекта!"))
        val studyGroup = server.readRequest() as StudyGroup
        HistoryWriter.writeObject(studyGroup)
        studyGroup.setId(id.toString())
        collection.add(studyGroup)
        return Response(ResponseStatus.SUCCESS, "Элемент успешно обновлён!\n")
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору и берёт данные для обновления из файла.
     * @param id идентификатор элемента для удаления.
     * @param reader для чтения данных из файла.
     */
    fun update(reader: BufferedReader, id: Long): Response {
        require(!idIsFree(id)) { "Элемент с id=$id не найден!\n" }
        collection.stream().filter { studyGroup: StudyGroup -> studyGroup.id == id }
            .forEach { o: StudyGroup? -> collection.remove(o) }
        val studyGroup = ObjectBuilder.create(reader)
        HistoryWriter.writeObject(studyGroup)
        studyGroup.setId(id.toString())
        collection.add(studyGroup)
        return Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент c id = $id успешно обновлён!\n")
    }

    /**
     * Удаляет все элементы, превышающие заданный.
     */
    fun removeGreater(): Response {
        if (collection.isEmpty()) return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
        server.sendResponse(Response(ResponseStatus.ASK_OBJECT, "Введите данные объекта для сравнения!"))

        val compareStudyGroup = server.readRequest() as StudyGroup
        HistoryWriter.writeObject(compareStudyGroup)
        compareStudyGroup.setId("0")

        collection.stream().filter { studyGroup: StudyGroup -> studyGroup > compareStudyGroup }
            .forEach { studyGroup: StudyGroup ->
                availableIds.add(studyGroup.id)
                collection.remove(studyGroup)
            }
        return Response(ResponseStatus.SUCCESS, "Элемент(ы) успешно удален(ы)!")
    }

    /**
     * Удаляет все элементы, превышающие тот, чьи данные берёт из файла.
     * @param reader для чтения данных из файла.
     */
    fun removeGreater(reader: BufferedReader): Response {
        if (collection.isEmpty()) return Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Коллекция пуста!")

        val compareStudyGroup = ObjectBuilder.create(reader)
        HistoryWriter.writeObject(compareStudyGroup)
        compareStudyGroup.setId("0")

        collection.stream().filter { studyGroup: StudyGroup -> studyGroup > compareStudyGroup }
            .forEach { studyGroup: StudyGroup ->
                availableIds.add(studyGroup.id)
                collection.remove(studyGroup)
            }
        return Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент(ы) успешно удален(ы)!")
    }

    /**
     * Выводит количество элементов, значение поля studentsCount которых меньше заданного.
     * @param studentsCount количество студентов.
     */
    fun countLess(studentsCount: Long): Response {
        return Response(
            ResponseStatus.SUCCESS,
            collection.stream().filter { studyGroup: StudyGroup -> studyGroup.studentsCount < studentsCount }
                .count().toString() + "\n")
    }

    /**
     * Выводит элементы коллекции в порядке убывания.
     */
    fun printDescending(): Response {
        if (collection.isEmpty()) return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
        collection.stream().sorted(Comparator.reverseOrder()).forEach { studyGroup: StudyGroup ->
            server.sendResponse(
                Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.toString())
            )
        }
        return Response(ResponseStatus.SUCCESS, "")
    }

    /**
     * Выводит значения поля formOfEducation всех элементов в порядке возрастания.
     */
    fun printFieldAscending(): Response {
        if (collection.isEmpty()) return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
        else {
            collection.stream().sorted().forEach { studyGroup: StudyGroup ->
                server.sendResponse(
                    Response(
                        ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.formOfEducation.toString()
                    )
                )
            }
            return Response(ResponseStatus.SUCCESS, "")
        }
    }

    /**
     * Выводит информацию о коллекции.
     */
    fun info(): Response {
        var info = """
             Тип коллекции: PriorityQueue
             Тип элементов: StudyGroup
             Количество элементов: ${collection.size}
             Дата инициализации: ${initializationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}
             
             """.trimIndent()
        info += if (lastSaveTime == null) "Сохранение коллекции ещё не осуществлялось."
        else "Дата последнего сохранения: " + lastSaveTime!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        return Response(ResponseStatus.SUCCESS, info + "\n")
    }

    private fun load() {
        try {
            dumpManager.loadCollection(loadFile)
        } catch (e: MissingFileException) {
            System.err.println(e.message)
        } catch (e: FileNotFoundException) {
            System.err.println("Файл $loadFile не найден.")
        } catch (e: SecurityException) {
            System.err.println("Нет прав доступа к файлу $loadFile")
        } catch (e: Exception) {
            System.err.println("Ошибка загрузки коллекции из файла" + loadFile + ": " + e.message)
        }
    }

    /**
     * Сохраняет элементы коллекции в файл, который передаётся из аргумента командной строки.
     */
    fun save(): Response {
        try {
            dumpManager.saveCollection(loadFile)
            lastSaveTime = LocalDateTime.now()
            return Response(ResponseStatus.SUCCESS, "Коллекция успешно сохранена!")
        } catch (e: MissingFileException) {
            System.err.println(e.message)
        } catch (e: SecurityException) {
            System.err.println("Нет прав доступа к файлу $loadFile")
        } catch (e: Exception) {
            System.err.println("Ошибка сохранения коллекция в файл" + loadFile + ": " + e.message)
        }
        return Response(ResponseStatus.ERROR, "При сохранении коллекции произошла ошибка")
    }
}
