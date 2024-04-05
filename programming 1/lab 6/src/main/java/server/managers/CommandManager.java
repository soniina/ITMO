package server.managers;

import common.communication.*;
import server.commands.*;
import server.network.Server;
import server.commands.Command;
import server.utils.HistoryWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.*;

/**
 * Менеджер, отвечающий за обработку команд и их выполнение.
 */
public class CommandManager {

    final private Map<String, Command> commands;
    final private Server server;
    private HistoryWriter historyWriter;

    public CommandManager(CollectionManager cm, Server server) {
        this.server = server;
        historyWriter = new HistoryWriter(server);

        commands = new HashMap<>();
        commands.put("help", new HelpCommand(commands));
        commands.put("add", new AddCommand(cm));
        commands.put("show", new ShowCommand(cm));
        commands.put("remove_by_id", new RemoveCommand(cm));
        commands.put("execute_script", new ExecuteScriptCommand(this));
        commands.put("exit", new ExitCommand(cm));
        commands.put("history", new HistoryCommand());
        commands.put("clear",  new ClearCommand(cm));
        commands.put("count_less_than_students_count", new CountLessCommand(cm));
        commands.put("head", new HeadCommand(cm));
        commands.put("info", new InfoCommand(cm));
        commands.put("print_descending", new PrintDescendingCommand(cm));
        commands.put("print_field_ascending_form_of_education", new PrintFieldAscendingCommand(cm));
        commands.put("remove_greater", new RemoveGreaterCommand(cm));
        commands.put("update", new UpdateCommand(cm));
    }

    /**
     * Выполняет указанную команду.
     */
    public Response execute(RequestCommand requestCommand) {
        Command command = commands.get(requestCommand.name());
        if (command != null) {
            String arg = requestCommand.arg();
            if (arg == null) arg = "";
            historyWriter.write(requestCommand.name() + " " + arg);
            try {
                return command.execute(requestCommand.arg());
            } catch (IllegalArgumentException e) {
                return new Response(ResponseStatus.ERROR, e.getMessage());
            }
        }
        else {
            return new Response(ResponseStatus.ERROR, "Команда не найдена! Введите 'help' для получения списка команд.\n");
        }
    }

    /**
     * Выполняет команду, требующую чтение данных элемента из файла.
     * @param line строка с командой и ее аргументами.
     * @param reader для чтения данных из файла.
     */
    public void execute(String line, BufferedReader reader) {
        String[] tokens = line.split(" ");
        Command command = commands.get(tokens[0]);
        if (command != null) {
            String arg = null;
            if (tokens.length > 1) arg = tokens[1];
            if (command instanceof Promptable) {
                try {
                    server.sendResponse(((Promptable) command).execute(reader, arg));
                } catch (IOException e) {
                    server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_ERROR, "Ошибка ввода-вывода: " + e.getMessage()));
                } catch (NullPointerException | exceptions.EnumConstantNotFoundException | DateTimeException |
                         IllegalArgumentException e) {
                    server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_ERROR, e.getMessage()));
                }
            }
            else {
                try {
                    Response response = command.execute(arg);
                    response.setStatus(ResponseStatus.INTERMEDIATE_SUCCESS);
                    server.sendResponse(response);
                } catch (IllegalArgumentException e) {
                    server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_ERROR, e.getMessage()));
                }
            }
        }
        else server.sendResponse(new Response(ResponseStatus.INTERMEDIATE_ERROR, "Команда не найдена! Введите 'help' для получения списка команд.\n"));
    }
}


