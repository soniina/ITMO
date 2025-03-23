package org.example.back4.resources

import com.datastax.oss.driver.api.core.CqlSession
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonCreator
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.example.back4.connection.CassandraSessionManager
import org.example.back4.dao.UserDAO
import org.example.back4.entity.User
import org.example.back4.utils.PasswordUtils
import org.example.back4.utils.TokenUtils

data class LoginRequest @JsonCreator constructor(
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String
)

@Path("/auth")
@ApplicationScoped
open class AuthResource {

    private val session: CqlSession = CassandraSessionManager.getSession()

    private val userDAO: UserDAO = UserDAO(session)

    private fun createResponse(status: Response.Status, entity: Any): Response {
        return Response.status(status)
            .entity(entity)
            .build()
    }

    @OPTIONS
    @Path("{path : .*}")
    open fun handlePreflight(): Response {
        return Response.ok().build()
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    open fun login(request: LoginRequest): Response {
        println("Received request: $request")
        val user = userDAO.getUserByUsername(request.username)
            ?: return createResponse(
                Response.Status.UNAUTHORIZED,
                "Пользователя с таким логином не существует"
            )
        if (!PasswordUtils.verifyPassword(request.password, user.password)) {
            return createResponse(
                Response.Status.UNAUTHORIZED,
                "Неверный пароль"
            )
        }
        val token = TokenUtils.generateToken(request.username)
        return createResponse(
            Response.Status.OK,
            mapOf("token" to token)
        )
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    open fun register(request: LoginRequest): Response {
        val user = userDAO.getUserByUsername(request.username)
        if (user != null) {
            return createResponse(
                Response.Status.UNAUTHORIZED,
                "Пользователь с таким логином уже существует"
            )
        }
        userDAO.saveUser(User(request.username, request.password))
        val token = TokenUtils.generateToken(request.username)
        return createResponse(
            Response.Status.OK,
            mapOf("token" to token)
        )
    }

}