package common.communication

import java.io.Serializable

/**
 * Класс ответа, отправляемого от сервера клиенту.
 */
class Response(var status: ResponseStatus, var descr: String?) : Serializable
