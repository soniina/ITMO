package client.controllers

import client.managers.GUIManager
import client.managers.ResponseManager
import client.models.CollectionModel
import client.network.Client
import client.views.AppForm
import common.communication.RequestCommand
import common.communication.Response
import common.communication.ResponseStatus
import common.models.StudyGroup
import javax.swing.SwingUtilities
import javax.swing.SwingWorker

class Controller(private val view: AppForm, private val model: CollectionModel, private val guiManager: GUIManager) {
    private val responseManager = ResponseManager(this)

    init {
        SwingUtilities.invokeLater {
            view.fillCollection(model.collection, model.myGroups)
            view.addExitButtonListener { guiManager.exit(view) }
            view.addHelpButtonListener { command -> ServerTask(command).execute() }
            view.addAddButtonListener { command -> ServerTask(command).execute() }
            view.addRemoveButtonListener { command -> ServerTask(command).execute() }
            view.addClearButtonListener { command -> ServerTask(command).execute() }
            view.addUpdateButtonListener { command -> ServerTask(command).execute() }
            view.addRemoveGreaterButtonListener { command -> ServerTask(command).execute() }
            view.addInfoButtonListener { command -> ServerTask(command).execute() }
            view.addHistoryButtonListener { command -> ServerTask(command).execute() }
            view.addCountLessButtonListener { command -> ServerTask(command).execute() }
            view.addExecuteScriptButtonListener { command -> ScriptTask(command).execute() }
        }
    }

    inner class ServerTask(command: String): SwingWorker<Response, Void>() {
        private val tokens = command.split(' ')
        private val arg = if (tokens.size > 1) tokens[1] else null

        override fun doInBackground(): Response {
            return sendCommand()
        }

        private fun sendCommand(): Response {
            Client.sendRequest(RequestCommand(tokens[0], arg))
            return Client.readResponse()
        }

        override fun done() {
            try {
                val response = get()
                responseManager.handle(response, arg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class ScriptTask(command: String): SwingWorker<ArrayList<Response>, Void>() {
        private val tokens = command.split(' ')
        private val arg = if (tokens.size > 1) tokens[1] else null

        override fun doInBackground(): ArrayList<Response> {
            var response: Response? = null
            val responses = ArrayList<Response>()
            Client.sendRequest(RequestCommand(tokens[0], arg))
            while (response == null || (response.status != ResponseStatus.SUCCESS && response.status != ResponseStatus.ERROR)) {
                response = Client.readResponse()
                responses.add(response)
            }
            return responses
        }

        override fun done() {
            try {
                val responses = get()
                for (response in responses) {
                    responseManager.handle(response, arg)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        model.fill()
        SwingUtilities.invokeLater {
            view.clearCollection()
            view.fillCollection(model.collection, model.myGroups)
        }
    }

    fun getFromCollection(id: Long): StudyGroup? {
        return model.get(id)
    }
}
