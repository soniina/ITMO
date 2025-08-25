package geometry

import kotlin.math.pow

class AreaCheck {
    fun isInArea(x: Int, y: Double, r: Int): Boolean {
        val firstCond = x <= 0 && y >= 0 &&
                y.pow(2) <= (r.toDouble()/2).pow(2) - x.toDouble().pow(2)
        val secondCond = x <= 0 && y <= 0 && y >= -(r.toDouble()/2) * x - r
        val thirdCond = x >= 0 && y <= 0 && x <= r.toDouble()/2 && y >= -r
        return (firstCond || secondCond || thirdCond)
    }
}