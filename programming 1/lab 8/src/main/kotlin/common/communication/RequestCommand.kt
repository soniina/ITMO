package common.communication

import java.io.Serializable

/**
 * Класс команды запроса, отправляемой от клиента к серверу.
 */
data class RequestCommand(val name: String, val arg: String?): Serializable {
    lateinit var token: String
}
