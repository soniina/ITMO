package client.localization

import client.views.AppForm
import java.util.*

class LocalizationManager {
    lateinit var appForm: AppForm
    private val subscribers = mutableListOf<Subscriber>()

    fun init() {
        appForm.addLanguageBoxListener { index ->
            val locale = when (index) {
                0 -> Locale("ru", "RU")
                1 -> Locale("ro", "RO")
                2 -> Locale("bg", "BG")
                3 -> Locale("en", "NZ")
                else -> Locale.getDefault()
            }
            Locale.setDefault(locale)
            notifySubscribers()
        }
    }

    fun subscribe(subscriber: Subscriber) {
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: Subscriber) {
        subscribers.remove(subscriber)
    }

    private fun notifySubscribers() {
        subscribers.forEach { subscriber -> subscriber.update()}
    }
}