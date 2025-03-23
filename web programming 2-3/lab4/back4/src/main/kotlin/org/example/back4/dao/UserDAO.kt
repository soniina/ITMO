package org.example.back4.dao

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import org.example.back4.entity.User
import org.example.back4.utils.PasswordUtils

open class UserDAO (private val session: CqlSession) {

    fun saveUser(user: User) {
        val query = "INSERT INTO users (username, password) VALUES (?, ?)"
        val statement = SimpleStatement.newInstance(
            query, user.username, PasswordUtils.hashPassword(user.password)
        )
        session.execute(statement)
    }

    fun getUserByUsername(username: String): User? {
        val query = "SELECT username, password FROM users WHERE username = ?"
        val resultSet = session.execute(SimpleStatement.newInstance(query, username))
        val row = resultSet.one()
        return row?.let {
            User(
                username = it.getString("username") ?: "",
                password = it.getString("password") ?: ""
            )
        }
    }
}
