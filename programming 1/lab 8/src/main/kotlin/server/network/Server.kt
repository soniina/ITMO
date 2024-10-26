package server.network

import common.utils.SerializationUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

/**
 * Серверное соединение для взаимодействия с клиентом по протоколу UDP.
 */
object Server {
    private val socket: DatagramSocket
    private var address: InetAddress? = null
    private val logger: Logger = LogManager.getLogger(Server::class.java)
    private var port = 3130

    /**
     * Конструктор класса Server, который создает серверный сокет и запускает сервер.
     */
    init {
        try {
            socket = DatagramSocket(3130)
            logger.info("Сервер запущен...")
        } catch (e: SocketException) {
            logger.error("Ошибка при запуске сервера: " + e.message)
            throw RuntimeException("Произошла ошибка при запуске сервера: $e")
        }
    }

    /**
     * Читает и возвращает запрос от клиента.
     */
    fun readRequest(): Any {
        try {
            val requestData = ByteArray(2048)
            val packet = DatagramPacket(requestData, requestData.size)
            socket.receive(packet)
            if (!(packet.address == address && packet.port == port)) {
                logger.info("Новый клиент подключён!")
                address = packet.address
                port = packet.port
            }
            logger.debug("Получен новый запрос от клиента")
            return SerializationUtils.deserialize(packet.data)
        } catch (e: IOException) {
            logger.error("Ошибка при получении данных от клиента: " + e.message)
            throw RuntimeException("Произошла ошибка при получении данных от клиента: " + e.message)
        }
    }

    /**
     * Отправляет ответ клиенту.
     * @param response ответ сервера клиенту.
     */
    fun sendResponse(response: Any?) {
        try {
            val responseData = SerializationUtils.serialize(response)
            val packet = DatagramPacket(responseData, responseData.size, address, port)
            socket.send(packet)
            logger.debug("Отправлен ответ на запрос клиента")
        } catch (e: IOException) {
            logger.error("Ошибка при отправке данных клиенту: " + e.message)
            throw RuntimeException("Произошла ошибка при отправке данных клиенту: $e")
        }
    }
}
