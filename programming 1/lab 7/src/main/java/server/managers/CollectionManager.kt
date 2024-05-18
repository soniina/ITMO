package server.managers

import common.communication.Response
import common.communication.ResponseStatus
import common.models.StudyGroup
import common.utils.ObjectBuilder
import server.network.Server
import server.utils.HistoryWriter
import java.io.BufferedReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Менеджер, оперирующий коллекцией
 */
class CollectionManager(private val dataBaseManager: DataBaseManager, private val threadPoolManager: ThreadPoolManager) {
    private val locker = ReentrantReadWriteLock()
    private val collection: PriorityQueue<StudyGroup> = dataBaseManager.load()
    private val initializationTime: LocalDateTime = LocalDateTime.now()

    /**
     * Добавляет новый элемент в коллекцию.
     */
    fun add(): Response {
        threadPoolManager.responseSenderExecutor.submit {
            Server.sendResponse(Response(ResponseStatus.ASK_OBJECT, "Введите данные добавляемого объекта!"))
        }
        val studyGroup = threadPoolManager.objectQueue.take()

        HistoryWriter.writeObject(studyGroup)
        locker.writeLock().lock()
        try {
            studyGroup.id = dataBaseManager.add(studyGroup)
            if (studyGroup.id > 0) {
                collection.add(studyGroup)
                return Response(ResponseStatus.SUCCESS, "Элемент успешно добавлен!\n")
            }
        } finally {
            locker.writeLock().unlock()
        }
        return Response(ResponseStatus.ERROR, "Произошла ошибка при добавлении элемента в базу данных!")
    }

    /**
     * Добавляет новый элемент в коллекцию на основе данных из файла.
     * @param reader для чтения данных из файла.
     */
    fun add(reader: BufferedReader): Response {
        val studyGroup = ObjectBuilder.create(reader)
        HistoryWriter.writeObject(studyGroup)

        locker.writeLock().lock()
        try {
            studyGroup.id = dataBaseManager.add(studyGroup)
            if (studyGroup.id > 0) {
                collection.add(studyGroup)
                return Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент успешно добавлен!\n")
            }
        } finally {
            locker.writeLock().unlock()
        }
        return Response(ResponseStatus.INTERMEDIATE_ERROR, "Произошла ошибка при добавлении элемента в базу данных!")
    }


    /**
     * Выводит содержимое коллекции.
     */
    fun show(): Response {
        var descr = ""
        locker.readLock().lock()
        try {
            if (collection.isEmpty()) descr = "Коллекция пуста!"
            collection.stream().sorted().forEach { studyGroup: StudyGroup ->
                val future = threadPoolManager.responseSenderExecutor.submit {
                    Server.sendResponse(Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.toString()))
                }
                future.get()
            }
        } finally {
            locker.readLock().unlock()
        }
        return Response(ResponseStatus.SUCCESS, descr)
    }

    /**
     * Очищает коллекцию.
     */
    fun clear(): Response {
        locker.writeLock().lock()
        try {
            dataBaseManager.clear()
            collection.clear()
        } finally {
            locker.writeLock().unlock()
        }
        return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
    }

    /**
     * Выводит первый элемент коллекции.
     */
    fun head(): Response {
        locker.readLock().lock()
        try {
            return if (collection.isEmpty()) Response(
                ResponseStatus.SUCCESS,
                "Коллекция пуста!"
            )
            else Response(ResponseStatus.SUCCESS, collection.peek().toString() + "\n")
        } finally {
            locker.readLock().unlock()
        }
    }

    /**
     * Удаляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для удаления.
     */
    fun remove(id: Long): Response {
        if (dataBaseManager.exist(id)) {
            if (dataBaseManager.checkCreatorId(id)) {
                locker.writeLock().lock()
                try {
                    if (dataBaseManager.remove(id)) {
                        val deleteGroup =
                            collection.stream().filter { studyGroup: StudyGroup -> studyGroup.id == id }.findFirst()
                                .get()
                        collection.remove(deleteGroup)
                        return Response(ResponseStatus.SUCCESS, "Элемент успешно удалён!\n")
                    } else return Response(
                        ResponseStatus.ERROR,
                        "Произошла ошибка при удалении элемента из базы данных!"
                    )
                } finally {
                    locker.writeLock().unlock()
                }
            } else return Response(ResponseStatus.ERROR, "Недостаточно прав для удаления объекта.\n")
        }
        return Response(ResponseStatus.ERROR, "Элемент c id=$id не найден.\n")
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору.
     * @param id идентификатор элемента для обновления.
     */
    fun update(id: Long): Response {
        if (dataBaseManager.exist(id)) {
            if (dataBaseManager.checkCreatorId(id)) {
                threadPoolManager.responseSenderExecutor.submit {
                    Server.sendResponse(Response(ResponseStatus.ASK_OBJECT, "Введите обновлённые данные объекта!"))
                }
                val studyGroup = threadPoolManager.objectQueue.take()
                locker.writeLock().lock()
                try {
                    if (dataBaseManager.update(id, studyGroup)) {
                        val deleteGroup =
                            collection.stream().filter { group: StudyGroup -> group.id == id }.findFirst()
                                .get()
                        collection.remove(deleteGroup)
                        HistoryWriter.writeObject(studyGroup)
                        studyGroup.id = id
                        collection.add(studyGroup)
                        return Response(ResponseStatus.SUCCESS, "Элемент успешно обновлён!\n")
                    } else return Response(
                        ResponseStatus.ERROR,
                        "Произошла ошибка при обновлении элемента в базе данных!"
                    )
                } finally {
                    locker.writeLock().unlock()
                }
            } else return Response(ResponseStatus.ERROR, "Недостаточно прав для обновления объекта.")
        } else
          return Response(ResponseStatus.ERROR, "Элемент c id=$id не найден.\n")
    }

    /**
     * Обновляет элемент из коллекции по указанному идентификатору и берёт данные для обновления из файла.
     * @param id идентификатор элемента для удаления.
     * @param reader для чтения данных из файла.
     */
    fun update(reader: BufferedReader, id: Long): Response {
        if (dataBaseManager.exist(id)) {
            if (dataBaseManager.checkCreatorId(id)) {
                val studyGroup = ObjectBuilder.create(reader)
                locker.writeLock().lock()
                try {
                    if (dataBaseManager.update(id, studyGroup)) {
                        val deleteGroup =
                            collection.stream().filter { group: StudyGroup -> group.id == id }.findFirst()
                                .get()
                        collection.remove(deleteGroup)
                        HistoryWriter.writeObject(studyGroup)
                        studyGroup.id = id
                        collection.add(studyGroup)
                        return Response(ResponseStatus.SUCCESS, "Элемент успешно обновлён!\n")
                    } else return Response(
                        ResponseStatus.ERROR,
                        "Произошла ошибка при обновлении элемента в базе данных!"
                    )
                } finally {
                    locker.writeLock().unlock()
                }
            } else return Response(ResponseStatus.ERROR, "Недостаточно прав для обновления объекта.")
        } else return Response(ResponseStatus.ERROR, "Элемент c id=$id не найден.\n")
    }

    /**
     * Удаляет все элементы, превышающие заданный.
     */
    fun removeGreater(): Response {
        if (collection.isEmpty()) return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
        threadPoolManager.responseSenderExecutor.submit {
            Server.sendResponse(Response(ResponseStatus.ASK_OBJECT, "Введите обновлённые данные объекта!"))
        }
        val compareStudyGroup = threadPoolManager.objectQueue.take()
        HistoryWriter.writeObject(compareStudyGroup)
        compareStudyGroup.id = 0
        locker.writeLock().lock()
        try {
        for (studyGroup in collection) {
            if (studyGroup > compareStudyGroup) {
                if (dataBaseManager.remove(studyGroup.id)) {
                    collection.remove(studyGroup)
                }
            }
        }
        } finally {
            locker.writeLock().unlock()
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
        compareStudyGroup.id = 0
        locker.readLock().lock()
        try {
            for (studyGroup in collection) {
                if (studyGroup > compareStudyGroup) {
                    if (dataBaseManager.checkCreatorId(studyGroup.id)) {
                        if (dataBaseManager.remove(studyGroup.id)) {
                            collection.remove(studyGroup)
                        }
                    }
                }
            }
        } finally {
            locker.readLock().unlock()
        }
        return Response(ResponseStatus.INTERMEDIATE_SUCCESS, "Элемент(ы) успешно удален(ы)!")
    }

    /**
     * Выводит количество элементов, значение поля studentsCount которых меньше заданного.
     * @param studentsCount количество студентов.
     */
    fun countLess(studentsCount: Long): Response {
        locker.readLock().lock()
        try {
            return Response(
                ResponseStatus.SUCCESS,
                collection.stream().filter { studyGroup: StudyGroup -> studyGroup.studentsCount < studentsCount }
                    .count().toString() + "\n")
        } finally {
            locker.readLock().unlock()
        }
    }

    /**
     * Выводит элементы коллекции в порядке убывания.
     */
    fun printDescending(): Response {
        locker.readLock().lock()
        try {
            if (collection.isEmpty()) return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
            collection.stream().sorted(Comparator.reverseOrder()).forEach { studyGroup: StudyGroup ->
                val future = threadPoolManager.responseSenderExecutor.submit {
                    Server.sendResponse(Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.toString()))
                }
                future.get()
            }
        } finally {
            locker.readLock().unlock()
        }
        return Response(ResponseStatus.SUCCESS, "")
    }

    /**
     * Выводит значения поля formOfEducation всех элементов в порядке возрастания.
     */
    fun printFieldAscending(): Response {
        locker.readLock().lock()
        try {
            if (collection.isEmpty()) return Response(ResponseStatus.SUCCESS, "Коллекция пуста!")
            collection.stream().sorted().forEach { studyGroup: StudyGroup ->
                val future = threadPoolManager.responseSenderExecutor.submit {
                    Server.sendResponse(Response(ResponseStatus.INTERMEDIATE_SUCCESS, studyGroup.formOfEducation.toString()))
                }
                future.get()
            }
            return Response(ResponseStatus.SUCCESS, "")
        } finally {
            locker.readLock().unlock()
        }
    }

    /**
     * Выводит информацию о коллекции.
     */
    fun info(): Response {
        locker.readLock().lock()
        try {
            val info = """
                 Тип коллекции: PriorityQueue
                 Тип элементов: StudyGroup
                 Количество элементов текущего пользователя: ${dataBaseManager.countUsersObjects()}
                 Количество всех элементов: ${collection.size}
                 Дата инициализации: ${initializationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}
                 """.trimIndent()
            return Response(ResponseStatus.SUCCESS, info + "\n")
        } finally {
            locker.readLock().unlock()
        }
    }
}
