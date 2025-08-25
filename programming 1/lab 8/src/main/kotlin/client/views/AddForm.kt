package client.views

import com.toedter.calendar.JDateChooser
import common.models.StudyGroup
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.net.URL
import java.time.ZoneId
import java.util.*
import javax.swing.*

class AddForm {
    private val r = ResourceBundle.getBundle("client.localization.GuiLabels", Locale.getDefault())

    private val frame = JFrame("Add studyGroup")
    private val submitButton = JButton(r.getString("submit"))
    private var closedBySubmit = false
    private val formPanel = JPanel(GridBagLayout())
    private val gbc = GridBagConstraints()
    val nameField = JTextField(20)
    val xField = JSpinner(SpinnerNumberModel(10.0, 10.0, 900.0, 0.1))
    val yField = JSpinner(SpinnerNumberModel(10.0f, 10.0f, 400.0f, 0.1f))
    val studentsCountField = JSpinner(SpinnerNumberModel(1L, 1L, 1e9.toLong(), 1L))
    val formOfEducationField = JComboBox(arrayOf("Очная", "Вечерняя", "Дистанционная"))
    val semesterField = JComboBox(arrayOf("-", 1, 3, 7, 8))
    private val adminDataLabel = JLabel(r.getString("groupAdminData"))
    val adminNameField = JTextField(20)
    val adminBirthdayField = JDateChooser().apply { dateFormatString = "dd.MM.yyyy" }
    val adminEyeColorField = JComboBox(arrayOf("-", "Синий", "Жёлтый", "Оранжевый", "Белый"))
    val adminNationalityField = JComboBox(arrayOf("Россия", "Великобритания", "США", "Северная Корея"))
    private val locationDataLabel = JLabel(r.getString("groupAdminLocationData"))
    val locationNameField = JTextField(20)
    val locationXField = JSpinner(SpinnerNumberModel(0.0, -1e9, 1e9, 0.1))
    val locationYField = JSpinner(SpinnerNumberModel(0L, (-1e9).toLong(), 1e9.toLong(), 1L))
    val locationZField = JSpinner(SpinnerNumberModel(0.0f, -1e9f, 1e9f, 0.1f))

    init {
        frame.setLocationRelativeTo(null)
        frame.layout = BorderLayout()
        frame.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE

        gbc.insets = Insets(5, 5, 5, 5)
        gbc.fill = GridBagConstraints.HORIZONTAL

        var row = 0

        addField(formPanel, gbc, row++, "${r.getString("groupName")}:", nameField)
        addField(formPanel, gbc, row++, "X:", xField)
        addField(formPanel, gbc, row++, "Y:", yField)
        addField(formPanel, gbc, row++, "${r.getString("studentsCount")}:", studentsCountField)
        addField(formPanel, gbc, row++, "${r.getString("formOfEducation")}:", formOfEducationField)
        addField(formPanel, gbc, row++, "${r.getString("semester")}:", semesterField)

        gbc.gridwidth = 2
        formPanel.add(JLabel(""), gbc)
        row++
        gbc.gridwidth = 1

        adminDataLabel.horizontalAlignment = SwingConstants.CENTER
        gbc.gridx = 0
        gbc.gridy = row++
        gbc.gridwidth = 2
        formPanel.add(adminDataLabel, gbc)
        gbc.gridwidth = 1

        addField(formPanel, gbc, row++, "${r.getString("name")}:", adminNameField)

        gbc.gridx = 0
        gbc.gridy = row
        gbc.anchor = GridBagConstraints.EAST

        formPanel.add(JLabel("${r.getString("birthday")}:"), gbc)
        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.WEST

        formPanel.add(adminBirthdayField, gbc)
        row++

        addField(formPanel, gbc, row++, "${r.getString("eyeColor")}:", adminEyeColorField)
        addField(formPanel, gbc, row++, "${r.getString("originCountry")}:", adminNationalityField)

        gbc.gridwidth = 2
        formPanel.add(JLabel(""), gbc)
        row++
        gbc.gridwidth = 1

        locationDataLabel.horizontalAlignment = SwingConstants.CENTER
        gbc.gridx = 0
        gbc.gridy = row++
        gbc.gridwidth = 2
        formPanel.add(locationDataLabel, gbc)
        gbc.gridwidth = 1

        addField(formPanel, gbc, row++, "${r.getString("locationName")}:", locationNameField)
        addField(formPanel, gbc, row++, "x:", locationXField)
        addField(formPanel, gbc, row++, "y:", locationYField)
        addField(formPanel, gbc, row, "z:", locationZField)

        frame.add(formPanel, BorderLayout.CENTER)

        frame.add(submitButton, BorderLayout.SOUTH)

        frame.pack()
        frame.isVisible = false
    }

    private fun addField(panel: JPanel, gbc: GridBagConstraints, row: Int, labelText: String, field: JComponent) {
        gbc.gridx = 0
        gbc.gridy = row
        gbc.anchor = GridBagConstraints.EAST
        panel.add(JLabel(labelText), gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.WEST
        panel.add(field, gbc)
    }

    fun showError(message: String) {
        val icon = ImageIcon(URL("https://i.pinimg.com/originals/37/bb/47/37bb47c7dcc6d5b13a26469270dd4472.gif"))
        val scaledImage = icon.image.getScaledInstance(100, 100, Image.SCALE_DEFAULT)
        JOptionPane.showMessageDialog(
            frame, message, "Error", JOptionPane.ERROR_MESSAGE, ImageIcon(scaledImage)
        )
    }

    fun addSubmitButtonListener(listener: () -> Unit) {
        submitButton.addActionListener {
            closedBySubmit = true
            listener()
        }
    }

    fun addExitButtonListener(listener: () -> Unit) {
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                val icon = ImageIcon(URL("https://i.pinimg.com/originals/37/bb/47/37bb47c7dcc6d5b13a26469270dd4472.gif"))
                val scaledImage = icon.image.getScaledInstance(100, 100, Image.SCALE_DEFAULT)
                val response = JOptionPane.showConfirmDialog(
                    frame,
                    r.getString("closingConfirmation"),
                    r.getString("confirmation"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, ImageIcon(scaledImage)
                )
                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose()
                }
            }

            override fun windowClosed(e: WindowEvent) {
                if (!closedBySubmit) listener()
            }
        })
    }

    fun show() {
        frame.isVisible = true
    }

    fun close() {
        frame.dispose()
    }

    fun fill(studyGroup: StudyGroup?) {
        if (studyGroup == null) return
        nameField.text = studyGroup.name
        xField.value = studyGroup.coordinates.x
        yField.value = studyGroup.coordinates.y
        studentsCountField.value = studyGroup.studentsCount
        formOfEducationField.selectedItem = studyGroup.formOfEducation

        if (studyGroup.semesterEnum == null) semesterField.selectedItem = "-"
        else semesterField.selectedItem = studyGroup.semesterEnum

        adminNameField.text = studyGroup.groupAdmin.name
        adminBirthdayField.date = Date.from(studyGroup.groupAdmin.birthday.atZone(ZoneId.systemDefault()).toInstant())
        adminNationalityField.selectedItem = studyGroup.groupAdmin.nationality
        if (studyGroup.groupAdmin.eyeColor == null) adminNationalityField.selectedItem = "-"
        else adminEyeColorField.selectedItem = studyGroup.groupAdmin.eyeColor

        locationNameField.text = studyGroup.groupAdmin.location.name
        locationXField.value = studyGroup.groupAdmin.location.x
        locationYField.value = studyGroup.groupAdmin.location.y
        locationZField.value = studyGroup.groupAdmin.location.z
    }
}

