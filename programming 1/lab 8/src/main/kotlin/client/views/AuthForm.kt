package client.views

import client.localization.Subscriber
import java.awt.Color
import java.util.*
import javax.swing.*

class AuthForm: Subscriber {
    private var r = ResourceBundle.getBundle("client.localization.GuiLabels", Locale("ru", "RU"))

    private val frame = JFrame("Authentication")
    private val groupLayout = GroupLayout(frame.contentPane)

    private val loginLabel = JLabel("${r.getString("login")}:")
    private val passwordLabel = JLabel("${r.getString("password")}:")
    private val loginField = JTextField(18)
    private val passwordField = JPasswordField(18)
    private val errorLabel = JLabel().apply { foreground = Color.RED; text = " " }
    private val loginButton = JButton(r.getString("signIn"))
    private val registerButton = JButton(r.getString("signUp"))

    init {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)

        frame.contentPane.layout = groupLayout
        groupLayout.autoCreateGaps = true
        groupLayout.autoCreateContainerGaps = true

        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    groupLayout.createSequentialGroup()
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(loginLabel)
                                .addComponent(passwordLabel)
                        )
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    loginField,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    Int.MAX_VALUE
                                )
                                .addComponent(
                                    passwordField,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    Int.MAX_VALUE
                                )
                        )
                )
                .addComponent(errorLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Int.MAX_VALUE)
                .addComponent(loginButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Int.MAX_VALUE)
                .addComponent(registerButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Int.MAX_VALUE)
        )

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(
                    groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginLabel)
                        .addComponent(loginField)
                )
                .addGroup(
                    groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField)
                )
                .addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Int.MAX_VALUE)
                .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Int.MAX_VALUE)
                .addComponent(registerButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Int.MAX_VALUE)
        )

        frame.pack()
        frame.isVisible = true
    }

    fun showError(message: String) {
        errorLabel.text = message
    }

    fun addLoginButterListener(listener: (login: String, password: String) -> Unit) {
        loginButton.addActionListener {
            listener(loginField.text, String(passwordField.password))
        }
    }

    fun addRegisterButterListener(listener: (login: String, password: String) -> Unit) {
        registerButton.addActionListener {
            listener(loginField.text, String(passwordField.password))
        }
    }

    fun clear() {
            loginField.text = null
            passwordField.text = null
    }

    fun open() {
            frame.isVisible = true
    }

    fun close() {
            errorLabel.text = " "
            frame.dispose()
    }

    override fun update() {
        r = ResourceBundle.getBundle("client.localization.GuiLabels", Locale.getDefault())

        loginLabel.text = "${r.getString("login")}:"
        passwordLabel.text = "${r.getString("password")}:"
        errorLabel.text = " "

        loginButton.text = r.getString("signIn")
        registerButton.text = r.getString("signUp")

        SwingUtilities.updateComponentTreeUI(frame)
    }

}