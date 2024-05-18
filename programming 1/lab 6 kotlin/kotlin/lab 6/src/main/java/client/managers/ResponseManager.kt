package client.managers

import client.network.Client
import common.communication.Response
import common.communication.ResponseStatus
import common.utils.ObjectBuilder
import kotlin.system.exitProcess

/**
 * Менеджер обработки ответов от сервера.
 */
class ResponseManager(private val client: Client) {
    /**
     * Обрабатывает ответ от сервера.
     * @param response ответ сервера.
     */
    fun handle(response: Response) {
        when (response.status) {
            ResponseStatus.SUCCESS, ResponseStatus.INTERMEDIATE_SUCCESS -> println(response.descr)
            ResponseStatus.ERROR, ResponseStatus.INTERMEDIATE_ERROR, ResponseStatus.HISTORY_OVERFLOW -> System.err.println(
                response.descr
            )

            ResponseStatus.ASK_OBJECT -> {
                client.sendRequest(ObjectBuilder.create())
            }

            ResponseStatus.EXIT -> {
                println(response.descr)
                exitProcess(0)
            }
//            else -> println("не учла папапаараша")
        }
    }
}
