package common.utils

import java.io.*

object SerializationUtils {
    fun  serialize(obj: Any?): ByteArray {
        try {
            ByteArrayOutputStream().use { byteStream ->
                ObjectOutputStream(byteStream).use { objectStream ->
                    objectStream.writeObject(obj)
                    objectStream.flush()
                    return byteStream.toByteArray()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Произошла ошибка при сериализации данных: $e")
        }
    }

    fun deserialize(data: ByteArray?): Any {
        try {
            ByteArrayInputStream(data).use { byteStream ->
                ObjectInputStream(byteStream).use { objectStream ->
                    return objectStream.readObject()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Произошла ошибка при десериализации данных: $e")
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("Класс для десериализации данных не найден: $e")
        }
    }
}
