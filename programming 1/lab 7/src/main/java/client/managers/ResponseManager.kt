package client.managers

import client.network.Client
import common.communication.Response
import common.communication.ResponseStatus
import common.utils.ObjectBuilder
import kotlin.system.exitProcess

/**
 * Менеджер обработки ответов от сервера.
 */
class ResponseManager {
    /**
     * Обрабатывает ответ от сервера.
     * @param response ответ сервера.
     */
    fun handle(response: Response) {
        when (response.status) {
            ResponseStatus.SUCCESS, ResponseStatus.INTERMEDIATE_SUCCESS -> println(response.descr)
            ResponseStatus.ERROR, ResponseStatus.INTERMEDIATE_ERROR, ResponseStatus.HISTORY_OVERFLOW -> println("\u001B[31m${response.descr}\u001B[0m")
            ResponseStatus.ASK_OBJECT -> {
                println(response.descr)
                Client.sendRequest(ObjectBuilder.create())
            }
            ResponseStatus.EXIT -> {
                println(response.descr)
                exitProcess(0)
            }
            ResponseStatus.TOKEN -> {}
            ResponseStatus.INVALID_TOKEN -> {
                exitProcess(0)
            }
        }
    }
}
