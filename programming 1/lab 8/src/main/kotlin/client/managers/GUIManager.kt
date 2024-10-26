package client.managers

import client.controllers.Controller
import client.localization.LocalizationManager
import client.models.CollectionModel
import client.views.AppForm
import client.views.AuthForm
import javax.swing.SwingUtilities

class GUIManager(private val authForm: AuthForm) {

    private val localizationManager = LocalizationManager()

    fun provideAccess(user: String) {
        SwingUtilities.invokeLater {
            val appForm = AppForm()
            val collectionModel = CollectionModel()
            val controller = Controller(appForm, collectionModel, this)
            localizationManager.appForm = appForm
            localizationManager.init()
            localizationManager.subscribe(authForm)
            localizationManager.subscribe(appForm)
            appForm.open(user)
        }
    }

    fun exit(appForm: AppForm) {
        SwingUtilities.invokeLater {
            appForm.close()
            authForm.open()
        }
    }
}