package beans

import dao.PointDAO
import entity.Point
import jakarta.annotation.PostConstruct
import jakarta.faces.application.FacesMessage
import jakarta.faces.context.FacesContext
import mbeans.ClickStatsMBean
import mbeans.PointStatsMBean
import validators.Validator
import java.io.Serializable
import java.lang.management.ManagementFactory
import javax.management.JMX
import javax.management.ObjectName
import kotlin.math.pow

class PointBean : Serializable {

    var x: String? = ""
    var y: String = ""
    var r: String = ""

    var lastPointJson = """{"id":-1}"""

    private val pointDAO = PointDAO()

    private val mbs = ManagementFactory.getPlatformMBeanServer()
    val pointStatsName = ObjectName("app.monitoring:type=PointStats")
    val pointProxy = JMX.newMBeanProxy(mbs, pointStatsName, PointStatsMBean::class.java)

    val clickStatsName = ObjectName("app.monitoring:type=ClickStats")
    val clickProxy = JMX.newMBeanProxy(mbs, clickStatsName, ClickStatsMBean::class.java)

    val results = mutableListOf<Point>()

    @PostConstruct
    fun init() {
        results.addAll(pointDAO.allPoints)
    }

    fun check(): Boolean {
        val validationResult = Validator().validate(x, y, r)
        if (validationResult.isValid) {
            val point = Point(x?.toDouble() ?: 0.0, y.toDouble(), r.toInt(), isInArea(x?.toDouble() ?: 0.0, y.toDouble(), r.toInt()))
            results.add(point)
            pointDAO.addPoint(point)
            pointProxy.addPoint(point.x, point.y, point.inArea)
            clickProxy.registerClick()
            lastPointJson = """{"x":${point.x},"y":${point.y},"r":${point.r},"inArea":${point.inArea}}"""
            return true
        }
        FacesContext.getCurrentInstance().addMessage(null,
            FacesMessage(validationResult.message)
        )
        lastPointJson = """{"id":-1}"""
        return false
    }

    private fun isInArea(x: Double, y: Double, r: Int): Boolean {
        val firstCond = x <= 0 && y >= 0 &&
                y.pow(2) <= (r.toDouble()/2).pow(2) - x.pow(2)
        val secondCond = x <= 0 && y <= 0 && y >= -2*x - r
        val thirdCond = x >= 0 && y <= 0 && x <= r.toDouble()/2 && y >= -r
        return (firstCond || secondCond || thirdCond)
    }
}