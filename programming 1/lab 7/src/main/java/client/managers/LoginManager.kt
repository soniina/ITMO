package client.managers


import client.network.Client
import common.communication.Response
import common.communication.ResponseStatus
import common.communication.UserInfo
import common.communication.UserStatus
import java.util.*

class LoginManager(private val responseManager: ResponseManager) {
    lateinit var token: String
    private val sc = Scanner(System.`in`)

    fun start() {
        println("Работа с коллекцией доступна только авторизованным пользователям!")
        while (true) {
            println("Вы зарегистрированы?")
            when (sc.nextLine().lowercase().trim()) {
                in arrayOf("да", "yes", "д", "y") -> {
                    authenticateUser(); break
                }

                in arrayOf("нет", "no", "н", "n") -> {
                    registerUser(); break
                }
                else -> println("\u001B[31mОтвет не распознан. Попробуйте еще раз!\u001B[0m")
            }
        }
    }


    private fun askLogin(): String {
        while (true) {
            print("Введите логин: ")
            val login = sc.nextLine()
            if (login.isEmpty()) println("\u001B[31mЛогин не может быть пустым! Попробуйте ещё раз.\u001B[0m")
            else return login
        }
    }

    private fun askPassword(): String {
        while (true) {
            print("Введите пароль: ")
            val password = sc.nextLine()
            //скрыть!!!!!!!!!
            if (password.isEmpty()) println("\u001B[31mПароль не может быть пустым! Попробуйте ещё раз.\u001B[0m")
            else return password
        }
    }

    private fun registerUser() {
        var response: Response? = null
        while (response == null || response.status != ResponseStatus.SUCCESS) {
            Client.sendRequest(UserInfo(askLogin(), askPassword(), UserStatus.REGISTRATION))
            response = Client.readResponse()
            responseManager.handle(response)
            if (response.status == ResponseStatus.SUCCESS) {
                token = Client.readResponse().descr
            } else println("Попробуйте ещё раз.")
        }
    }

    private fun authenticateUser() {
        var response: Response? = null
        while (response == null || response.status != ResponseStatus.SUCCESS) {
            Client.sendRequest(UserInfo(askLogin(), askPassword(), UserStatus.AUTHENTICATION))
            response = Client.readResponse()
            responseManager.handle(response)
            if (response.status == ResponseStatus.SUCCESS) {
                token = Client.readResponse().descr
            } else println("Попробуйте ещё раз.")
        }
    }
}