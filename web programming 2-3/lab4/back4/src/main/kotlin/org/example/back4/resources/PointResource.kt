package org.example.back4.resources

import com.datastax.oss.driver.api.core.CqlSession
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.example.back4.connection.CassandraSessionManager
import org.example.back4.dao.PointDAO
import org.example.back4.validators.Validator
import org.example.back4.entity.Point
import org.example.back4.filters.Secured
import kotlin.math.pow

@Path("/point")
@ApplicationScoped
@Secured
open class PointResource {
    private val validator = Validator()

    private val session: CqlSession = CassandraSessionManager.getSession()

    private val pointDAO: PointDAO = PointDAO(session)

    private fun createResponse(status: Response.Status, entity: Any): Response {
        return Response.status(status)
            .entity(entity)
            .build()
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    open fun getAllPoints(): Response {
        val points = pointDAO.getAllPoints()
        return createResponse(Response.Status.OK, points)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    open fun processPoint(
        @QueryParam("x") x: String,
        @QueryParam("y") y: String,
        @QueryParam("r") r: String
    ): Response {
        val validationResult = validator.validate(x, y, r)
        if (!validationResult.isValid) {
            return createResponse(Response.Status.BAD_REQUEST, validationResult.message)
        }
        val point = Point(x = x.toDouble(), y = y.toDouble(), r = r.toInt(), inArea = isInArea(x.toDouble(), y.toDouble(), r.toInt()))
        pointDAO.savePoint(point)

        return createResponse(Response.Status.OK, point)
    }

    private fun isInArea(x: Double, y: Double, r: Int): Boolean {
        val firstCond = x <= 0 && y >= 0 &&
                y.pow(2) <= (r.toDouble()/2).pow(2) - x.pow(2)
        val secondCond = x <= 0 && y <= 0 && y >= -2*x - r
        val thirdCond = x >= 0 && y <= 0 && x <= r.toDouble()/2 && y >= -r
        return (firstCond || secondCond || thirdCond)
    }
}