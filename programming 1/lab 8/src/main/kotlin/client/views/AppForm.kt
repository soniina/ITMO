package client.views

import client.graphics.Circle
import client.localization.Subscriber
import common.models.StudyGroup
import java.awt.*
import java.io.File
import java.net.URL
import java.util.*
import javax.swing.*
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter
import kotlin.collections.ArrayList

class AppForm: Subscriber {

    private var r = ResourceBundle.getBundle("client.localization.GuiLabels", Locale.getDefault())

    private val userLabel = JLabel("${r.getString("user")}: ")
    private val userLogin = JLabel()
    private val languageLabel = JLabel("${r.getString("language")}: ")
    private val languageComboBox = JComboBox(arrayOf("Русский", "Română", "Български", "English (NZ)")).apply {
        selectedIndex = when (Locale.getDefault()) {
            Locale("ru", "RU") -> 0
            Locale("ro", "RO") -> 1
            Locale("bg", "BG") -> 2
            Locale("en", "NZ") -> 3
            else -> 4
        }
    }

    private val exitButton = JButton(r.getString("exit"))
    private val helpButton = JButton(r.getString("help"))
    private val header = JPanel().apply {
        layout = BorderLayout()

        add(JPanel().apply {
            layout = FlowLayout(FlowLayout.LEFT)
            add(userLabel)
            add(userLogin)
        }, BorderLayout.WEST)

        add(JPanel().apply {
            layout = FlowLayout(FlowLayout.RIGHT)
            add(languageLabel)
            add(languageComboBox)
            add(helpButton)
            add(exitButton)
        }, BorderLayout.EAST)
    }

    private val addButton = JButton(r.getString("add"))
    private val removeButton = JButton(r.getString("remove"))
    private val updateButton = JButton(r.getString("update"))
    private val clearButton = JButton(r.getString("clear"))
    private val removeGreaterButton = JButton(r.getString("removeGreater"))

    private val modifierButtons = JPanel().apply {
        layout = FlowLayout(FlowLayout.CENTER)
        add(addButton)
        add(removeButton)
        add(updateButton)
        add(clearButton)
        add(removeGreaterButton)
    }
    private val tableModel = object : DefaultTableModel(arrayOf("id", r.getString("groupName"), "X", "Y", r.getString("creationDate"), r.getString("studentsCount"), r.getString("formOfEducation"), r.getString("semester"), r.getString("name") + " " + r.getString("ofAdmin"), r.getString("birthday") + " " + r.getString("ofAdmin"), r.getString("eyeColor") + " " + r.getString("ofAdmin"), r.getString("originCountry") + " " + r.getString("ofAdmin"), r.getString("location") + " " + r.getString("ofAdmin"), "x", "y", "z"), 0) {
        override fun isCellEditable(row: Int, column: Int): Boolean {
            return false
        }
    }

    private val table = JTable(tableModel).apply {
        rowSorter = TableRowSorter(tableModel).apply {
            setComparator(0, Comparator<Long> {n1, n2 -> n1.compareTo(n2)})
            setComparator(2, Comparator<Double> {n1, n2 -> n1.compareTo(n2)})
            setComparator(3, Comparator<Float> {n1, n2 -> n1.compareTo(n2)})
            setComparator(5, Comparator<Long> {n1, n2 -> n1.compareTo(n2)})
            setComparator(13, Comparator<Double> {n1, n2 -> n1.compareTo(n2)})
            setComparator(14, Comparator<Long> {n1, n2 -> n1.compareTo(n2)})
            setComparator(15, Comparator<Float> {n1, n2 -> n1.compareTo(n2)})
        }
        columnModel.getColumn(0).preferredWidth = 30
        columnModel.getColumn(1).preferredWidth = 80
        columnModel.getColumn(2).preferredWidth = 40
        columnModel.getColumn(3).preferredWidth = 40
        columnModel.getColumn(4).preferredWidth = 100
        columnModel.getColumn(5).preferredWidth = 50
        columnModel.getColumn(6).preferredWidth = 100
        columnModel.getColumn(7).preferredWidth = 50
        columnModel.getColumn(8).preferredWidth = 60
        columnModel.getColumn(9).preferredWidth = 100
        columnModel.getColumn(10).preferredWidth = 60
        columnModel.getColumn(11).preferredWidth = 60
        columnModel.getColumn(12).preferredWidth = 60
        columnModel.getColumn(13).preferredWidth = 40
        columnModel.getColumn(14).preferredWidth = 40
        columnModel.getColumn(15).preferredWidth = 40
    }

    private val scrollTable = JScrollPane(table)

    private val visualization = MapPanel()
    private val displayCollection = JTabbedPane().apply {
        addTab(r.getString("table"), scrollTable)
        addTab(r.getString("map"), visualization)
    }
    private val body = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(displayCollection)
        add(modifierButtons)
    }

    private val countLessLabel = JLabel(r.getString("countLess"))
    private val countLessNField = JTextField(2)
    private val countLessStudentsLabel = JLabel(r.getString("lessStudents"))
    private val countLessButton = JButton(r.getString("count"))
    private val countLessForm = JPanel().apply {
        layout = FlowLayout(FlowLayout.LEFT)
        add(countLessLabel)
        add(countLessNField)
        add(countLessStudentsLabel)
    }

    private val executeScriptButton = JButton(r.getString("executeScript"))
    private val historyButton = JButton(r.getString("history"))
    private val infoButton = JButton(r.getString("info"))

    private val otherButtons = JPanel(GridBagLayout()).apply {
        val gbc = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.NORTH
            weightx = 1.0
        }
        gbc.gridy = 0
        gbc.insets = Insets(29, 2, 2, 5)
        add(infoButton, gbc)
        gbc.gridy = 1
        gbc.insets = Insets(2, 2, 2, 5)
        add(historyButton, gbc)
        gbc.gridy = 2
        add(executeScriptButton, gbc)
        gbc.gridy = 3
        add(countLessForm, gbc)
        gbc.gridy = 4
        add(countLessButton, gbc)
        gbc.gridy = 5
        gbc.weighty = 1.0
        add(Box.createVerticalGlue(), gbc)
    }

    private val frame = JFrame("meow").apply {
        isVisible = false
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        add(header, BorderLayout.NORTH)
        add(body, BorderLayout.CENTER)
        add(otherButtons, BorderLayout.EAST)
        setSize(1200, 600)
    }


    fun open(user: String) {
            frame.isVisible = true
            userLogin.text = user
    }

    fun close() {
            frame.dispose()
    }

    fun addExitButtonListener(listener: () -> Unit) {
        exitButton.addActionListener {
            listener()
        }
    }

    fun addHelpButtonListener(listener: (command: String) -> Unit) {
        helpButton.addActionListener {
            listener("help")
        }
    }

    fun addLanguageBoxListener(listener: (Int) -> Unit) {
        languageComboBox.addItemListener {
            listener(languageComboBox.selectedIndex)
        }
    }

    fun addAddButtonListener(listener: (command: String) -> Unit) {
        addButton.addActionListener {listener("add")}
    }

    fun addRemoveButtonListener(listener: (command: String) -> Unit) {
        removeButton.addActionListener {
            if (displayCollection.selectedIndex == 0) {
                val selectedGroup = table.selectedRow
                if (selectedGroup != -1) {
                    listener("remove_by_id ${table.getValueAt(selectedGroup, 0)}")
                } else {
                    showError(r.getString("itemIsNotSelected"))
                }
            }
            else {
                val selectedGroup = visualization.selectedCircle
                if (selectedGroup != null) {
                    listener("remove_by_id ${selectedGroup.text.toLong()}")
                } else {
                    showError(r.getString("itemIsNotSelected"))
                }
            }
        }
    }

    fun addUpdateButtonListener(listener: (command: String) -> Unit) {
        updateButton.addActionListener {
            if (displayCollection.selectedIndex == 0) {
                val selectedGroup = table.selectedRow
                if (selectedGroup != -1) {
                    listener("update ${table.getValueAt(selectedGroup, 0)}")
                } else {
                    showError(r.getString("itemIsNotSelected"))
                }
            }
            else {
                val selectedGroup = visualization.selectedCircle
                if (selectedGroup != null) {
                    listener("update ${selectedGroup.text.toLong()}")
                } else {
                    showError(r.getString("itemIsNotSelected"))
                }
            }
        }
    }

    fun addClearButtonListener(listener: (command: String) -> Unit) {
        clearButton.addActionListener {listener("clear")}
    }

    fun addRemoveGreaterButtonListener(listener: (command: String) -> Unit) {
        removeGreaterButton.addActionListener {listener("remove_greater")}
    }

    fun addCountLessButtonListener(listener: (command: String) -> Unit) {
        countLessButton.addActionListener {
            if (countLessNField.text.isNotEmpty())
                listener("count_less_than_students_count ${countLessNField.text}")
            else showError(r.getString("enterNumberOfStudents"))
        }
    }

    fun addExecuteScriptButtonListener(listener: (command: String) -> Unit) {
        executeScriptButton.addActionListener {
            val fileChooser = JFileChooser()
            val currentDir = File(".").canonicalFile
            fileChooser.currentDirectory = currentDir
            val result = fileChooser.showOpenDialog(frame)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFile = fileChooser.selectedFile.canonicalFile
                val relativePath = currentDir.toPath().relativize(selectedFile.toPath()).toString()
                listener("execute_script $relativePath")
            }
        }
    }

    fun addHistoryButtonListener(listener: (command: String) -> Unit) {
        historyButton.addActionListener {listener("history")}
    }

    fun addInfoButtonListener(listener: (command: String) -> Unit) {
        infoButton.addActionListener {listener("info")}
    }

    private fun showError(message: String) {
        val icon = ImageIcon(URL("https://i.pinimg.com/originals/37/bb/47/37bb47c7dcc6d5b13a26469270dd4472.gif"))
        val scaledImage = icon.image.getScaledInstance(100, 100, Image.SCALE_DEFAULT)
        JOptionPane.showMessageDialog(JFrame(), message, "Error", JOptionPane.ERROR_MESSAGE, ImageIcon(scaledImage))
    }

    private var nowCollection = PriorityQueue<StudyGroup>()
    private var nowMyGroups = ArrayList<Long>()

    fun fillCollection(collection: PriorityQueue<StudyGroup>, myGroups: ArrayList<Long>) {
        val tempCollection = PriorityQueue(collection)
        while (!tempCollection.isEmpty()) {
            val studyGroup = tempCollection.poll()
            tableModel.addRow(
                arrayOf(
                    studyGroup.id,
                    studyGroup.name,
                    studyGroup.coordinates.x,
                    studyGroup.coordinates.y,
                    studyGroup.creationDate,
                    studyGroup.studentsCount,
                    studyGroup.formOfEducation.form,
                    studyGroup.semesterEnum?.name,
                    studyGroup.groupAdmin.name,
                    studyGroup.groupAdmin.birthday.toLocalDate(),
                    studyGroup.groupAdmin.eyeColor?.color,
                    studyGroup.groupAdmin.nationality.country,
                    studyGroup.groupAdmin.location.name,
                    studyGroup.groupAdmin.location.x,
                    studyGroup.groupAdmin.location.y,
                    studyGroup.groupAdmin.location.z
                )
            )

            val info = """
                <html>
                ${r.getString("groupName")}: ${studyGroup.name}<br>
                X: ${studyGroup.coordinates.x}<br>
                Y: ${studyGroup.coordinates.y}<br>
                ${r.getString("creationDate")}: ${studyGroup.creationDate}<br>
                ${r.getString("studentsCount")}: ${studyGroup.studentsCount}<br>
                ${r.getString("formOfEducation")}: ${studyGroup.formOfEducation.form}<br>
                ${r.getString("semester")}: ${studyGroup.semesterEnum?.name}<br>
                ${r.getString("name")} ${r.getString("ofAdmin")}: ${studyGroup.groupAdmin.name}<br>
                ${r.getString("birthday")} ${r.getString("ofAdmin")}: ${studyGroup.groupAdmin.birthday.toLocalDate()}<br>
                ${r.getString("eyeColor")} ${r.getString("ofAdmin")}: ${studyGroup.groupAdmin.eyeColor?.color}<br>
                ${r.getString("originCountry")} ${r.getString("ofAdmin")}: ${studyGroup.groupAdmin.nationality.country}<br>
                ${r.getString("location")} ${r.getString("ofAdmin")}: ${studyGroup.groupAdmin.location.name}<br>
                x: ${studyGroup.groupAdmin.location.x}<br>
                y: ${studyGroup.groupAdmin.location.y}<br>
                z: ${studyGroup.groupAdmin.location.z}
                </html>
            """.trimIndent()

            if (studyGroup.id in myGroups) {
                if (nowCollection.contains(studyGroup)) {
                    visualization.addCircle(
                        Circle(
                            studyGroup.coordinates.x,
                            studyGroup.coordinates.y,
                            studyGroup.studentsCount,
                            Color(32, 227, 49, 175),
                            "${studyGroup.id}",
                            info
                        )
                    )
                }
                else {
                    visualization.addCircleWithAnimation(
                        Circle(
                            studyGroup.coordinates.x,
                            studyGroup.coordinates.y,
                            studyGroup.studentsCount,
                            Color(32, 227, 49, 175),
                            "${studyGroup.id}",
                            info
                        )
                    )
                }
            } else {
                if (nowCollection.contains(studyGroup)) {
                    visualization.addCircle(
                        Circle(
                            studyGroup.coordinates.x,
                            studyGroup.coordinates.y,
                            studyGroup.studentsCount,
                            Color(255, 66, 66, 175),
                            "${studyGroup.id}",
                            info
                        )
                    )
                }
                else {
                    visualization.addCircleWithAnimation(
                        Circle(
                            studyGroup.coordinates.x,
                            studyGroup.coordinates.y,
                            studyGroup.studentsCount,
                            Color(255, 66, 66, 175),
                            "${studyGroup.id}",
                            info
                        )
                    )
                }
            }
        }
        nowCollection = PriorityQueue(collection)
        nowMyGroups = ArrayList(myGroups)
    }

    fun clearCollection() {
        tableModel.rowCount = 0
        visualization.clear()
    }


    override fun update() {
        r = ResourceBundle.getBundle("client.localization.GuiLabels", Locale.getDefault())

        frame.title = "meow"
        userLabel.text = "${r.getString("user")}: "
        languageLabel.text = "${r.getString("language")}: "

        exitButton.text = r.getString("exit")
        helpButton.text = r.getString("help")
        addButton.text = r.getString("add")
        removeButton.text = r.getString("remove")
        updateButton.text = r.getString("update")
        clearButton.text = r.getString("clear")
        removeGreaterButton.text = r.getString("removeGreater")
        countLessButton.text = r.getString("count")
        executeScriptButton.text = r.getString("executeScript")
        historyButton.text = r.getString("history")
        infoButton.text = r.getString("info")

        countLessLabel.text = r.getString("countLess")
        countLessStudentsLabel.text = r.getString("lessStudents")

        val columnNames = arrayOf(
            "id",
            r.getString("groupName"),
            "X",
            "Y",
            r.getString("creationDate"),
            r.getString("studentsCount"),
            r.getString("formOfEducation"),
            r.getString("semester"),
            "${r.getString("name")} ${r.getString("ofAdmin")}",
            "${r.getString("birthday")} ${r.getString("ofAdmin")}",
            "${r.getString("eyeColor")} ${r.getString("ofAdmin")}",
            "${r.getString("originCountry")} ${r.getString("ofAdmin")}",
            "${r.getString("location")} ${r.getString("ofAdmin")}",
            "x",
            "y",
            "z"
        )

        for (i in columnNames.indices) {
            table.columnModel.getColumn(i).headerValue = columnNames[i]
        }

        displayCollection.setTitleAt(0, r.getString("table"))
        displayCollection.setTitleAt(1, r.getString("map"))

        clearCollection()
        fillCollection(nowCollection, nowMyGroups)

        SwingUtilities.updateComponentTreeUI(frame)
    }
}