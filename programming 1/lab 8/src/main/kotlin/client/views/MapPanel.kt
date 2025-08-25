package client.views

import client.graphics.Circle
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import kotlin.math.max
import kotlin.math.min


class MapPanel : JPanel() {

    private val backgroundImage: Image = ImageIcon("src/main/resources/images/map.png").image
    private val circles: MutableList<Circle> = ArrayList()
    var selectedCircle: Circle? = null

    init {
        ToolTipManager.sharedInstance().initialDelay = 0

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                for (circle in circles.sortedBy { circle: Circle -> circle.diameter }) {
                    if (circle.contains(e.x, e.y)) {
                        if (selectedCircle == circle) selectedCircle = null
                        else {
                            selectedCircle = circle
                            toolTipText = circle.info
                            ToolTipManager.sharedInstance().mouseMoved(e)
                        }
                        break
                    } else { toolTipText = null }
                }
                repaint()
            }
        })
    }


    fun addCircle(circle: Circle) {
        circles.add(circle)
        repaint()
    }

    fun addCircleWithAnimation(circle: Circle) {
        var currentDiameter = 0L
        val newCircle = Circle(circle.x, circle.y, currentDiameter, circle.color, circle.text, circle.info)
        circles.add(newCircle)

        val animationTimer = Timer(10) { e ->
            if (currentDiameter < circle.diameter) {
                currentDiameter++
                newCircle.diameter = currentDiameter
                repaint()
            } else {
                (e.source as Timer).stop()
            }
        }
        animationTimer.start()
    }

    fun clear() {
        circles.clear()
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(backgroundImage, 0, 0, width, height, this)
        for (circle in circles) {
            val diameter = max(min(circle.diameter, 250), 10)
            val radius = diameter / 2
            if (circle == selectedCircle) {
                g.color = Color.YELLOW
                g.fillOval(
                    (circle.x - 8 - radius).toInt(),
                    (circle.y - 8 - radius).toInt(),
                    diameter.toInt() + 16,
                    diameter.toInt() + 16
                )
            }
            g.color = circle.color
            g.fillOval((circle.x - radius).toInt(), (circle.y - radius).toInt(), diameter.toInt(), diameter.toInt())

            val fontSize = min(max(7, (circle.diameter / 5)), 100).toInt()
            val font = Font("Arial", Font.BOLD, fontSize)
            g.font = font

            g.color = Color.BLACK
            val fm = g.getFontMetrics(font)
            val textWidth = fm.stringWidth(circle.text)
            val textHeight = fm.height
            val textX = circle.x - textWidth / 2
            val textY = circle.y + (textHeight / 2) - fm.descent
            g.drawString(circle.text, textX.toInt(), textY.toInt())
        }
    }
}
