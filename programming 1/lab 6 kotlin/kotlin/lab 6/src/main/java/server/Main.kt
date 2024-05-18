package server

import common.communication.RequestCommand
import common.models.StudyGroup
import server.commands.SaveCommand
import server.managers.CollectionManager
import server.managers.CommandManager
import server.network.Server
import server.utils.HistoryWriter
import java.util.*

/**
 * Основной класс приложения, отвечающий за запуск серверной части программы.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val collection = PriorityQueue<StudyGroup>()
        var loadFile: String? = null
        if (args.isNotEmpty()) loadFile = args[0]

        val server = Server()

        val collectionManager = CollectionManager(collection, loadFile, server)
        val commandManager = CommandManager(collectionManager, server)
        HistoryWriter.init(server)

        val scanner = Scanner(System.`in`)

        while (true) {
            val requestCommand = server.readRequest() as RequestCommand
            val response = commandManager.execute(requestCommand)
            server.sendResponse(response)
            if (System.`in`.available() > 0) {
                val line = scanner.nextLine().trim()
                if (line.isNotEmpty()) {
                    val tokens = line.split(' ')
                    var arg: String? = null
                    if (tokens.size > 1) arg = tokens[1]
                    try {
                        if (tokens[0] == "save") SaveCommand(collectionManager).execute(arg)
                        else System.err.println("Недоступная команда!")
                    } catch (e: IllegalArgumentException) {
                        System.err.println(e.message)
                    }
                }
            }
        }
    }
}