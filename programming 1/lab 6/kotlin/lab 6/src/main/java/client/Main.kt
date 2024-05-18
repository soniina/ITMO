package client

import client.managers.ResponseManager
import client.network.Client
import common.communication.RequestCommand
import common.communication.Response
import common.communication.ResponseStatus
import java.util.*

/**
 * Основной класс приложения, отвечающий за запуск клиентской части программы.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val sc = Scanner(System.`in`)
        val client = Client()
        val responseManager = ResponseManager(client)

        while (sc.hasNext()) {
            val line = sc.nextLine().trim()
            if (line.isNotEmpty()) {
                val tokens = line.split(' ')
                var arg: String? = null
                if (tokens.size > 1) arg = tokens[1]
                val request = RequestCommand(tokens[0], arg)
                client.sendRequest(request)
                var response: Response? = null
                while (response == null || (response.status != ResponseStatus.SUCCESS && response.status != ResponseStatus.ERROR)) {
                    response = client.readResponse()
                    responseManager.handle(response)
                }
            }
        }
        sc.close()
        client.close()
    }
}
