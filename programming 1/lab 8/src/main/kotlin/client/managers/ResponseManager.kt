package client.managers

import client.controllers.Controller
import client.network.Client
import client.views.AddForm
import common.communication.Response
import common.communication.ResponseStatus
import common.models.StudyGroup
import common.utils.ObjectBuilder
import java.awt.Image
import java.net.URL
import javax.swing.*
import kotlin.system.exitProcess

class ResponseManager(private val controller: Controller) {

    fun handle(response: Response, arg: String?) {
        when (response.status) {
            ResponseStatus.ASK_OBJECT -> {
                SwingUtilities.invokeLater {
                    val addForm = AddForm()
                    if (arg != null) {
                        addForm.fill(controller.getFromCollection(arg.toLong()))
                    }
                    addForm.addSubmitButtonListener {
                        val studyGroup = ObjectBuilder.create(addForm)
                        if (studyGroup != null) {
                            AddTask(studyGroup, addForm).execute()
                        }
                    }
                    addForm.addExitButtonListener {
                        AddTask(StudyGroup(), addForm).execute()
                    }
                    addForm.show()
                }
            }
            ResponseStatus.SUCCESS, ResponseStatus.INTERMEDIATE_SUCCESS -> {
                SwingUtilities.invokeLater {
                    val icon = ImageIcon(URL("https://i.pinimg.com/originals/37/bb/47/37bb47c7dcc6d5b13a26469270dd4472.gif"))
                    val scaledImage = icon.image.getScaledInstance(100, 100, Image.SCALE_DEFAULT)
                    JOptionPane.showMessageDialog(JFrame(), JLabel("<html>${response.descr.replace("\n", "<br>")}</html>").apply { horizontalAlignment = SwingConstants.CENTER  }, "Success", JOptionPane.INFORMATION_MESSAGE, ImageIcon(scaledImage))
                }
            }
            ResponseStatus.ERROR, ResponseStatus.INTERMEDIATE_ERROR, ResponseStatus.HISTORY_OVERFLOW -> {
                SwingUtilities.invokeLater {
                    val icon = ImageIcon(URL("https://i.pinimg.com/originals/37/bb/47/37bb47c7dcc6d5b13a26469270dd4472.gif"))
                    val scaledImage = icon.image.getScaledInstance(100, 100, Image.SCALE_DEFAULT)
                    JOptionPane.showMessageDialog(JFrame(), JLabel("<html>${response.descr.replace("\n", "<br>")}</html>").apply { horizontalAlignment = SwingConstants.CENTER  }, "Error", JOptionPane.ERROR_MESSAGE, ImageIcon(scaledImage))
                }
            }
            ResponseStatus.TOKEN -> {}
            ResponseStatus.INVALID_TOKEN -> {
                exitProcess(0)
            }
        }
        if (response.status != ResponseStatus.INTERMEDIATE_SUCCESS && response.status != ResponseStatus.INTERMEDIATE_ERROR) {
            controller.update()
        }
    }

    inner class AddTask(private val studyGroup: StudyGroup, private val addForm: AddForm): SwingWorker<Response, Void>() {

        override fun doInBackground(): Response {
            Client.sendRequest(studyGroup)
            return Client.readResponse()
        }

        override fun done() {
            SwingUtilities.invokeLater {
                addForm.close()
                if (studyGroup.name != "") handle(get(), null)
            }
        }
    }
}
