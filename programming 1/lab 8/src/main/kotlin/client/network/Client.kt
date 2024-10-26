package client.network

import common.communication.RequestCommand
import common.communication.Response
import common.models.StudyGroup
import common.utils.SerializationUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector

/**
 * Клиентское соединение для взаимодействия с сервером по протоколу UDP.
 */
object Client {
    private const val PORT = 3130
    private val channel: DatagramChannel
    private val selector: Selector
    private val buffer: ByteBuffer
    var token: String? = null

    /**
     * Конструктор класса Client, который создает клиентское соединение и настраивает канал для передачи данных.
     */
    init {
        try {
            channel = DatagramChannel.open()
            channel.configureBlocking(false)
            selector = Selector.open()
            channel.register(selector, SelectionKey.OP_WRITE)
            buffer = ByteBuffer.allocate(2048)
        } catch (e: IOException) {
            throw RuntimeException("Произошла ошибка при настройке канала передачи данных: $e")
        }
    }

    /**
     * Отправляет запрос на сервер.
     * @param obj объект, который необходимо отправить на сервер.
     */
    fun sendRequest(obj: Any?) {
        if (obj is RequestCommand) {
            while (token == null) Thread.sleep(10)
            val token = token
            obj.token = token!!
        }
        val sendData = SerializationUtils.serialize(obj)
        buffer.clear()
        buffer.put(sendData)
        buffer.flip()

        var attempt = 1
        while (attempt <= 3) {
            try {
                channel.send(buffer, InetSocketAddress("localhost", PORT))

                if (selector.select(5000) != 0) {
                    channel.register(selector, SelectionKey.OP_READ)
                    return
                }
            } catch (e: IOException) {
                throw RuntimeException("Произошла ошибка при передаче данных на сервер: $e")
            }
            attempt += 1
        }
        throw RuntimeException("Превышено количество попыток отправки запроса. Сервер временно недоступен!")
    }

    /**
     * Читает и возвращает ответ от сервера.
     */
    fun readResponse(): Response {
        var attempt = 1
        while (attempt <= 3) {
            try {
                selector.select()
                val keys = selector.selectedKeys()
                val iterator = keys.iterator()

                while (iterator.hasNext()) {
                    val key = iterator.next()
                    iterator.remove()

                    if (key.isReadable) {
                        buffer.clear()
                        channel.receive(buffer)
                        val resp = SerializationUtils.deserialize(buffer.array()) as Response
                        return resp
                    }
                }
                Thread.sleep(5000)
            } catch (e: IOException) {
                throw RuntimeException("Произошла ошибка при получении данных с сервера: $e")
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            attempt += 1
        }
        throw RuntimeException("Превышено количество попыток получения ответа. Сервер временно недоступен!")
    }

    fun readObject(): StudyGroup  {
        var attempt = 1
        while (attempt <= 3) {
            try {
                selector.select()
                val keys = selector.selectedKeys()
                val iterator = keys.iterator()

                while (iterator.hasNext()) {
                    val key = iterator.next()
                    iterator.remove()

                    if (key.isReadable) {
                        buffer.clear()
                        channel.receive(buffer)
                        return SerializationUtils.deserialize(buffer.array()) as StudyGroup
                    }
                }
                Thread.sleep(5000)
            } catch (e: IOException) {
                throw RuntimeException("Произошла ошибка при получении данных с сервера: $e")
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            attempt += 1
        }
        throw RuntimeException("Превышено количество попыток получения ответа. Сервер временно недоступен!")
    }
}
