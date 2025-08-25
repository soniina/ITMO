package mbeans

import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener
import java.lang.management.ManagementFactory
import javax.management.MBeanServer
import javax.management.ObjectName

class MBeanRegistrar : ServletContextListener {

    override fun contextInitialized(sce: ServletContextEvent?) {
        val mbs: MBeanServer = ManagementFactory.getPlatformMBeanServer()
        val pointStats = PointStats()
        val clickStats = ClickStats()

        val pointStatsName = ObjectName("app.monitoring:type=PointStats")
        val clickStatsName = ObjectName("app.monitoring:type=ClickStats")

        if (!mbs.isRegistered(pointStatsName)) {
            mbs.registerMBean(pointStats, pointStatsName)
        }

        if (!mbs.isRegistered(clickStatsName)) {
            mbs.registerMBean(clickStats, clickStatsName)
        }
    }
}
