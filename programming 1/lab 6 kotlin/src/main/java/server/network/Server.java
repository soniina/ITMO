package server.network;

import common.communication.*;
import common.utils.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.utils.HistoryWriter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Серверное соединение для взаимодействия с клиентом по протоколу UDP.
 */
public class  Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static int port = 3130;
    private final DatagramSocket socket;
    private InetAddress address = null;

    /**
     * Конструктор класса Server, который создает серверный сокет и запускает сервер.
     */
    public Server() {
        try {
            socket = new DatagramSocket(3130);
            logger.info("Сервер запущен...");
        } catch (SocketException e) {
            logger.error("Ошибка при запуске сервера: " + e.getMessage());
            throw new RuntimeException("Произошла ошибка при запуске сервера: " + e);
        }
    }

    /**
     * Читает и возвращает запрос от клиента.
     */
    public Object readRequest() {
        try {
            byte[] requestData = new byte[2048];
            DatagramPacket packet = new DatagramPacket(requestData, requestData.length);
            socket.receive(packet);
            if (!(packet.getAddress() == address && packet.getPort() == port)) {
                logger.info("Новый клиент подключён!");
                address = packet.getAddress();
                port = packet.getPort();
                HistoryWriter.setClient(address + ":" + port);
            }
            logger.debug("Получен новый запрос от клиента");
            return SerializationUtils.deserialize(packet.getData());
        } catch (IOException e) {
            logger.error("Ошибка при получении данных от клиента: " + e.getMessage());
            throw new RuntimeException("Произошла ошибка при получении данных от клиента: " + e.getMessage());
        }
    }

    /**
     * Отправляет ответ клиенту.
     * @param response ответ сервера клиенту.
     */
    public void sendResponse(Response response) {
        try {
            byte[] responseData = SerializationUtils.serialize(response);
            DatagramPacket packet = new DatagramPacket(responseData, responseData.length, address, port);
            socket.send(packet);
            logger.debug("Отправлен ответ на запрос клиента");
        } catch (IOException e) {
            logger.error("Ошибка при отправке данных клиенту: " + e.getMessage());
            throw new RuntimeException("Произошла ошибка при отправке данных клиенту: " + e);
        }
    }
}
