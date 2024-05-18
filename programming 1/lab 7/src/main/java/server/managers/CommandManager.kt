package server.managers

import common.communication.RequestCommand
import common.communication.Response
import common.communication.ResponseStatus
import client.exceptions.EnumConstantNotFoundException
import server.commands.*
import server.network.Server
import server.utils.HistoryWriter
import java.io.BufferedReader
import java.io.IOException
import java.time.DateTimeException

/**
 * Менеджер, отвечающий за обработку команд и их выполнение.
 */
class CommandManager(cm: CollectionManager, private val threadPoolManager: ThreadPoolManager) {
    private val commands: MutableMap<String, Command> = HashMap()

    init {
        commands["help"] = HelpCommand(commands)
        commands["add"] = AddCommand(cm)
        commands["show"] = ShowCommand(cm)
        commands["remove_by_id"] = RemoveCommand(cm)
        commands["execute_script"] = ExecuteScriptCommand(this)
        commands["exit"] = ExitCommand(cm)
        commands["history"] = HistoryCommand()
        commands["clear"] = ClearCommand(cm)
        commands["count_less_than_students_count"] = CountLessCommand(cm)
        commands["head"] = HeadCommand(cm)
        commands["info"] = InfoCommand(cm)
        commands["print_descending"] = PrintDescendingCommand(cm)
        commands["print_field_ascending_form_of_education"] = PrintFieldAscendingCommand(cm)
        commands["remove_greater"] = RemoveGreaterCommand(cm)
        commands["update"] = UpdateCommand(cm)
    }

    /**
     * Выполняет указанную команду.
     */
    fun execute(requestCommand: RequestCommand): Response {
        val command = commands[requestCommand.name]
        if (command != null) {
            val arg = requestCommand.arg
            if (arg == null) HistoryWriter.write(requestCommand.name)
            else HistoryWriter.write(requestCommand.name + " " + arg)
            return try {
                command.execute(requestCommand.arg)
            } catch (e: IllegalArgumentException) {
                Response(ResponseStatus.ERROR, e.message!!)
            }
        } else {
            return Response(ResponseStatus.ERROR, "Команда не найдена! Введите 'help' для получения списка команд.\n")
        }
    }

    /**
     * Выполняет команду, требующую чтение данных элемента из файла.
     * @param line строка с командой и ее аргументами.
     * @param reader для чтения данных из файла.
     */
    fun execute(line: String, reader: BufferedReader) {
        val tokens = line.split(' ')
        val command = commands[tokens[0]]
        if (command != null) {
            var arg: String? = null
            if (tokens.size > 1) arg = tokens[1]
            if (command is Promptable) {
                val response =
                    try {
                        (command as Promptable).execute(reader, arg)
                    } catch (e: IOException) {
                        Response(ResponseStatus.INTERMEDIATE_ERROR, "Ошибка ввода-вывода: " + e.message)
                    } catch (e: NullPointerException) {
                        Response(ResponseStatus.INTERMEDIATE_ERROR, e.message!!)
                    } catch (e: EnumConstantNotFoundException) {
                        Response(ResponseStatus.INTERMEDIATE_ERROR, e.message!!)
                    } catch (e: DateTimeException) {
                        Response(ResponseStatus.INTERMEDIATE_ERROR, e.message!!)
                    } catch (e: IllegalArgumentException) {
                        Response(ResponseStatus.INTERMEDIATE_ERROR, e.message!!)
                    }
                threadPoolManager.responseSenderExecutor.submit {
                    Server.sendResponse(response)
                }
            } else {
                val response =
                    try {
                        Response(ResponseStatus.INTERMEDIATE_SUCCESS, command.execute(arg).descr)
                    } catch (e: IllegalArgumentException) {
                        Response(ResponseStatus.INTERMEDIATE_ERROR, e.message!!)
                    }
                threadPoolManager.responseSenderExecutor.submit {
                    Server.sendResponse(response)
                }
            }
        } else {
            threadPoolManager.responseSenderExecutor.submit {
            Server.sendResponse(Response(
                    ResponseStatus.INTERMEDIATE_ERROR,
                    "Команда не найдена! Введите 'help' для получения списка команд.\n"))
            }
        }
    }
}


