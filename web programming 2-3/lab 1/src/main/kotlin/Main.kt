import com.fastcgi.FCGIInterface
import handlers.RequestHandler

fun main() {
    val requestHandler = RequestHandler()
    while (FCGIInterface().FCGIaccept() >= 0) {
        requestHandler.start()
    }
}
