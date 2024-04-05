package common.utils;

import java.io.*;

public class SerializationUtils {
    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
            objectStream.flush();
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при сериализации данных: " + e);
        }
    }

    public static Object deserialize(byte[] data) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            return objectStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при десериализации данных: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Класс для десериализации данных не найден: " + e);
        }
    }
}
