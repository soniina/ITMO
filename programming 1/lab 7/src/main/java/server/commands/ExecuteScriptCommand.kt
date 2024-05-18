package server.commands

import common.communication.Response
import common.communication.ResponseStatus
import server.exceptions.RecursionScriptException
import server.managers.CommandManager
import java.io.*
import java.util.*


/**
 * Команда для исполнения скрипта из файла.
 */
class ExecuteScriptCommand(private val commandManager: CommandManager) :
    Command("execute_script file_name", "исполнить скрипт из файла") {
    private val scriptStack = Stack<String>()

    /**
     * Исполняет команды из указанного файла.
     * @param arg файл скрипта.
     */
    override fun execute(arg: String?): Response {
        var file = arg
        requireNotNull(file) { "Команда ${name.split(' ')[0]} требует аргумент ${name.split(' ')[1]}" }
        if (file == "history1" || file == "history2" || file == "history3") {
            try {
                BufferedReader(FileReader(file)).use { reader ->
                    BufferedWriter(FileWriter(file + "_copy")).use { writer ->
                        var line: String?
                        var previousLine: String? = null
                        while ((reader.readLine().also { line = it }) != null) {
                            if (previousLine != null) {
                                writer.write(previousLine)
                                writer.newLine()
                            }
                            previousLine = line
                        }
                        file += "_copy"
                    }
                }
            } catch (e: IOException) {
                return Response(ResponseStatus.ERROR, "Ошибка ввода-вывода: " + e.message)
            }
        }

        try {
            BufferedReader(FileReader(file)).use { reader ->
                var line: String?
                if ((!(scriptStack.isEmpty()) && (scriptStack.peek() == file)) || scriptStack.contains(file)) throw RecursionScriptException()
                scriptStack.push(file)
                while ((reader.readLine().also { line = it }) != null) {
                    commandManager.execute(line!!, reader)
                }
                if (scriptStack.peek() == file) scriptStack.pop()
                else throw RecursionScriptException()
                return Response(ResponseStatus.SUCCESS, "Скрипт успешно выполнен!")
            }
        } catch (e: FileNotFoundException) {
            return Response(ResponseStatus.ERROR, "Файл $file не найден =(\n")
        } catch (e: SecurityException) {
            return Response(ResponseStatus.ERROR, "Нет прав доступа к файлу $file: ${e.message}")
        } catch (e: IOException) {
            return Response(ResponseStatus.ERROR, "Ошибка ввода-вывода: ${e.message}")
        } catch (e: EmptyStackException) {
            return Response(ResponseStatus.ERROR, "Рекурсивный скрипт!")
        } catch (e: RecursionScriptException) {
            return Response(ResponseStatus.ERROR, "Рекурсивный скрипт!")
        } finally {
            if (file == "history1_copy" || file == "history2_copy" || file == "history3_copy") File(file).delete()
        }
    }
}

