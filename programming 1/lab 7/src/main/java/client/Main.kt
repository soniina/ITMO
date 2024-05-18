package client

import client.managers.LoginManager
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
        val responseManager = ResponseManager()
        val loginManager = LoginManager(responseManager)

        loginManager.start()

        while (sc.hasNext()) {
            val line = sc.nextLine().trim()
            if (line.isNotEmpty()) {
                val tokens = line.split(' ')
                var arg: String? = null
                if (tokens.size > 1) arg = tokens[1]
                val request = RequestCommand(tokens[0], arg, loginManager.token)
                Client.sendRequest(request)
                var response: Response? = null
                while (response == null || (response.status != ResponseStatus.SUCCESS && response.status != ResponseStatus.ERROR)) {
                    response = Client.readResponse()
                    responseManager.handle(response)
                }
            }
        }
        sc.close()
        Client.close()
    }
}
