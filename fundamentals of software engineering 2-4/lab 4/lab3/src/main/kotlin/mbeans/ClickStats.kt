package mbeans

interface ClickStatsMBean {
    fun registerClick()
    fun getAverageClickInterval(): Double
}

class ClickStats:  ClickStatsMBean {
    var lastClickTime = -1L
    var totalInterval = 0.0
    var clickCount = 0

    override fun registerClick() {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != -1L) {
            totalInterval += currentTime - lastClickTime
            clickCount++
        }
        lastClickTime = currentTime
    }

    override fun getAverageClickInterval(): Double {
        return if (clickCount == 0) 0.0 else totalInterval / clickCount
    }
}