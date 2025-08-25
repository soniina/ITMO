package mbeans

import javax.management.Notification
import javax.management.NotificationBroadcasterSupport

interface PointStatsMBean {
    fun addPoint(x: Double, y: Double, inArea: Boolean)
    fun getTotalPoints(): Int
    fun getOutsidePoints(): Int
}

class PointStats: NotificationBroadcasterSupport(), PointStatsMBean {

    private var totalPoints = 0
    private var outsidePoints = 0
    var sequence = 1L

    override fun addPoint(x: Double, y: Double, inArea: Boolean) {
        totalPoints++
        if (!inArea) {
            outsidePoints++
            val notification: Notification = Notification(
                "point.outside", this, sequence++,
                System.currentTimeMillis(),
                "Point is outside: ($x, $y)"
            )
            sendNotification(notification)
        }
    }

    override fun getTotalPoints(): Int = totalPoints

    override fun getOutsidePoints(): Int = outsidePoints
}