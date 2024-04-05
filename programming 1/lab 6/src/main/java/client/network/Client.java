package client.network;

import common.communication.Response;
import common.utils.SerializationUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Клиентское соединение для взаимодействия с сервером по протоколу UDP.
 */
public class Client {
    private final DatagramChannel channel;
    private final Selector selector;
    private final ByteBuffer buffer;
    private static final int port = 3130;

    /**
     * Конструктор класса Client, который создает клиентское соединение и настраивает канал для передачи данных.
     */
    public Client() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_WRITE);
            buffer = ByteBuffer.allocate(2048);
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при настройке канала передачи данных: " + e);
        }
    }

    /**
     * Отправляет запрос на сервер.
     * @param object объект, который необходимо отправить на сервер.
     */
    public void sendRequest(Object object) {
        byte[] sendData = SerializationUtils.serialize(object);
        buffer.clear();
        buffer.put(sendData);
        buffer.flip();

        for (int attempt = 1; attempt <= 3; attempt += 1) {
            try {
                channel.send(buffer, new InetSocketAddress("localhost", port));

                if (selector.select(5000) != 0) {
                    channel.register(selector, SelectionKey.OP_READ);
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException("Произошла ошибка при передаче данных на сервер: " + e);
            }
        }
        throw new RuntimeException("Превышено количество попыток отправки запроса. Сервер временно недоступен!");
    }

    /**
     * Читает и возвращает ответ от сервера.
     */
    public Response readResponse() {
        for (int attempt = 1; attempt <= 3; attempt += 1) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {
                        buffer.clear();
                        channel.receive(buffer);
                        return (Response) SerializationUtils.deserialize(buffer.array());
                    }
                }
                Thread.sleep(5000);
            } catch (IOException e) {
                throw new RuntimeException("Произошла ошибка при получении данных с сервера: " + e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Превышено количество попыток получения ответа. Сервер временно недоступен!");
    }

    /**
     * Закрывает клиентское соединение.
     */
    public void close() {
        try {
            if (selector != null) selector.close();
            if (channel != null) channel.close();
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при закрытии канала передачи данных: " + e);
        }
    }
}
