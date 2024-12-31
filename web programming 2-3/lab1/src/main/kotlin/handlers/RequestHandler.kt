package handlers

import com.fastcgi.FCGIInterface
import java.nio.charset.StandardCharsets
import dto.Response
import geometry.AreaCheck
import java.util.*

enum class Status(val code: Int) { SUCCESS(200), INVALID(400)}

class RequestHandler {

    private val validator = Validator()
    private val areaChecker = AreaCheck()

    fun start() {
        val currentTime = Date().time
        val requestParams = FCGIInterface.request.params
        val query = requestParams.getProperty("QUERY_STRING")
        val (x, y, r) = query.split("&").map {
            it.split("=").last().replace(',', '.')
        }
        val validateResult = validator.validate(x, y, r)
        if (validateResult.isValid) {
            sendSuccess(
                Response(
                    validateResult.message,
                    areaChecker.isInArea(x.toInt(), y.toDouble(), r.toInt()),
                ), Date().time - currentTime
            )
        } else {
            sendError(Response(validateResult.message))
        }
    }

    private fun sendSuccess(response: Response, time: Long) {
        val content = """{"message": "${response.message}","isInArea": ${response.isInArea}, "time": $time}"""
        val contentLength = content.toByteArray(StandardCharsets.UTF_8).size

        val httpResponse = """
        HTTP/1.1 ${Status.SUCCESS.code}
        Content-Type: application/json
        Content-Length: $contentLength

        $content
    """.trimIndent()
        println(httpResponse)
    }

    private fun sendError(response: Response) {
        val content = """{"message": "${response.message}"}"""
        val contentLength = content.toByteArray(StandardCharsets.UTF_8).size

        val httpResponse = """
        HTTP/1.1 ${Status.INVALID.code}
        Content-Type: application/json
        Content-Length: $contentLength

        $content
    """.trimIndent()
        println(httpResponse)
    }
}