package server.managers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import common.communication.Response
import common.communication.ResponseStatus
import org.postgresql.util.PSQLException
import server.network.Server
import java.security.MessageDigest
import java.security.SecureRandom
import java.sql.Connection
import java.util.*

class UserManager(private val connection: Connection) {
    private val pepper = "V2138^%4#9Ux"
    private val algorithm = Algorithm.HMAC256("verbasus")

    fun registerUser(login: String, password: String): Response {
        try {
            val statement = connection.prepareStatement("INSERT INTO users (login, password, salt) VALUES (?, ?, ?);")
            statement.setString(1, login)
            val salt = generateSalt()
            statement.setString(3, salt)
            statement.setString(2, hashPassword(password, salt))
            statement.executeUpdate()
            return authenticateUser(login, password)
        } catch (e: PSQLException) {
            return Response(ResponseStatus.ERROR, "Пользователь с таким логином уже есть!")
        }
    }

    fun authenticateUser(login: String, password: String): Response {
        val statement = connection.prepareStatement( "SELECT id, password, salt FROM users WHERE login = ?;")
        statement.setString(1, login)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            if (hashPassword(password, resultSet.getString("salt")) == resultSet.getString("password")) {
                Server.sendResponse(Response(ResponseStatus.SUCCESS, "Вы успешно вошли в систему!"))
                val setToken = connection.prepareStatement("UPDATE users SET token = ? WHERE login = ?;")
                val token = generateToken(resultSet.getInt("id"), login)
                setToken.setString(1, token)
                setToken.setString(2, login)
                setToken.executeUpdate()
                return Response(ResponseStatus.TOKEN, token)
            }
            else return Response(ResponseStatus.ERROR, "Неверный пароль!")
        }
        else {
            return Response(ResponseStatus.ERROR, "Пользователь с таким логином не найден!")
        }
    }

    private fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("MD2")
        val hash = md.digest((password + pepper + salt).toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

    private fun generateToken(id: Int, login: String): String {
        return JWT.create()
            .withSubject(login)
            .withClaim("id", id)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
            .sign(algorithm)
    }

    private fun generateSalt(): String {
        val salt = ByteArray(10)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt).substring(0, 10)
    }

    fun verifyToken(token: String): Long {
        try {
            JWT.require(algorithm).build().verify(token)
            return JWT.decode(token).getClaim("id").asLong()
        } catch (e: JWTVerificationException) { return 0 }
    }
}