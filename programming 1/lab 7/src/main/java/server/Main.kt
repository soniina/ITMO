package server

import common.communication.*
import common.models.StudyGroup
import server.managers.*
import server.network.Server
import server.utils.HistoryWriter
import java.util.concurrent.LinkedBlockingQueue


/**
 * Основной класс приложения, отвечающий за запуск серверной части программы.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {

        val requestQueue = LinkedBlockingQueue<Any>()

        val threadPoolManager = ThreadPoolManager()
        val dataBaseManager = DataBaseManager()
        val collectionManager = CollectionManager(dataBaseManager, threadPoolManager)
        val userManager = UserManager(dataBaseManager.connection)
        val commandManager = CommandManager(collectionManager, threadPoolManager)

        while (true) {
            threadPoolManager.requestReaderExecutor.submit {
                requestQueue.put(Server.readRequest())
            }
            val request = requestQueue.take()
            threadPoolManager.requestProcessorExecutor.submit {
                when (request) {
                    is RequestCommand -> {
                        val response: Response
                        val userId = userManager.verifyToken(request.token)
                        if (userId != 0L) {
                            dataBaseManager.userId.set(userId)
                            HistoryWriter.setClient(userId)
                            response = commandManager.execute(request)
                        } else response = Response(ResponseStatus.INVALID_TOKEN, "Срок действия вашей сессии истек или токен недействителен.")
                        threadPoolManager.responseSenderExecutor.submit {
                            Server.sendResponse(response)
                        }
                        dataBaseManager.userId.remove()
                    }

                    is UserInfo -> {
                        val response =
                            if (request.status == UserStatus.REGISTRATION)
                                userManager.registerUser(request.login, request.password)
                            else userManager.authenticateUser(request.login, request.password)
                        threadPoolManager.responseSenderExecutor.submit {
                            Server.sendResponse(response)
                        }
                    }

                    is StudyGroup -> {
                        threadPoolManager.objectQueue.put(request)
                    }
                }
            }
        }
    }
}