package client.controllers

import client.managers.GUIManager
import client.network.Client
import client.views.AuthForm
import common.communication.*
import javax.swing.SwingUtilities

class AuthController(authForm: AuthForm, guiManager: GUIManager) {

    init {
        SwingUtilities.invokeLater {
            authForm.addLoginButterListener { login, password ->
                if (login == "") authForm.showError("Логин не может быть пустым!")
                else if (password == "") authForm.showError("Пароль не может быть пустым!")
                else {
                    val response = loginUser(login, password)
                    if (response.status == ResponseStatus.SUCCESS) {
                        authForm.close()
                        guiManager.provideAccess(login)
                    } else {
                        authForm.showError(response.descr)
                    }
                    authForm.clear()
                }
            }
            authForm.addRegisterButterListener { login, password ->
                if (login == "") authForm.showError("Логин не может быть пустым!")
                else if (password == "") authForm.showError("Пароль не может быть пустым!")
                else {
                    val response = registerUser(login, password)
                    if (response.status == ResponseStatus.SUCCESS) {
                        authForm.close()
                        guiManager.provideAccess(login)
                    } else {
                        authForm.showError(response.descr)
                    }
                    authForm.clear()
                }
            }
        }
    }

    private fun registerUser(login: String, password: String): Response {
        Client.sendRequest(UserInfo(login, password, UserStatus.REGISTRATION))
        val response = Client.readResponse()
        if (response.status == ResponseStatus.SUCCESS) {
            Client.token = Client.readResponse().descr
        }
        return response
    }

    private fun loginUser(login: String, password: String): Response {
        Client.sendRequest(UserInfo(login, password, UserStatus.AUTHENTICATION))
        val response = Client.readResponse()
        if (response.status == ResponseStatus.SUCCESS) {
            Client.token = Client.readResponse().descr
        }
        return response
    }
}