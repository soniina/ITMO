package client

import client.controllers.AuthController
import client.managers.GUIManager
import client.views.AuthForm
import java.awt.Color
import javax.swing.SwingUtilities
import javax.swing.UIManager


fun main() {
    UIManager.put("ToolTip.background", Color.WHITE)
    SwingUtilities.invokeLater {
        val authForm = AuthForm()
        val guiManager = GUIManager(authForm)
        val authController = AuthController(authForm, guiManager)
    }
}