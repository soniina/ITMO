package org.example.back4.dao

import org.example.back4.entity.Point
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import java.util.*

open class PointDAO (private val session: CqlSession) {

    fun savePoint(point: Point) {
        val query = "INSERT INTO points (id, x, y, r, in_area) VALUES (?, ?, ?, ?, ?)"
        val statement = SimpleStatement.newInstance(
            query, point.id, point.x, point.y, point.r, point.inArea
        )
        session.execute(statement)
    }

    fun getAllPoints(): List<Point> {
        val query = "SELECT id, x, y, r, in_area FROM points"
        val resultSet = session.execute(query)

        return resultSet.map { row ->
            Point(
                id = row.getUuid("id") ?: UUID.randomUUID(),
                x = row.getDouble("x"),
                y = row.getDouble("y"),
                r = row.getInt("r"),
                inArea = row.getBoolean("in_area")
            )
        }.toList()
    }
}
