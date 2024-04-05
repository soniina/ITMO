package managers;

import commands.*;
import utils.HistoryWriter;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Менеджер, отвечающий за обработку команд и их выполнение.
 */
public class CommandManager {

    final private Map<String, Command> commands;
    final private List<String> commandHistory;
    final private HistoryWriter historyWriter;

    public CommandManager(CollectionManager collectionManager) {

        commandHistory = new ArrayList<>();
        historyWriter = new HistoryWriter();
        historyWriter.clear();

        commands = new HashMap<>();
        commands.put("help", new HelpCommand(commands));
        commands.put("info", new InfoCommand(collectionManager));
        commands.put("add", new AddCommand(collectionManager));
        commands.put("history", new HistoryCommand(commandHistory));
        commands.put("show", new ShowCommand(collectionManager));
        commands.put("clear", new ClearCommand(collectionManager));
        commands.put("exit", new ExitCommand());
        commands.put("head", new HeadCommand(collectionManager));
        commands.put("remove_by_id", new RemoveCommand(collectionManager));
        commands.put("update", new UpdateCommand(collectionManager));
        commands.put("remove_greater", new RemoveGreaterCommand(collectionManager));
        commands.put("count_less_than_students_count", new CountLessCommand(collectionManager));
        commands.put("print_descending", new PrintDescendingCommand(collectionManager));
        commands.put("print_field_ascending_form_of_education", new PrintFieldAscendingCommand(collectionManager));
        commands.put("save", new SaveCommand(collectionManager));
        commands.put("execute_script", new ExecuteScriptCommand(this));
    }

    /**
     * Выполняет указанную команду.
     * @param line строка с командой и ее аргументами.
     */
    public void execute(String line) {
        String[] tokens = line.split(" ");
        Command command = commands.get(tokens[0]);
        if (command != null) {
            historyWriter.write(line);
            commandHistory.add(line);
            if (tokens.length > 1) {
                command.execute(tokens[1]);
            } else {
                command.execute(null);
            }
        }
        else System.err.println("Команда не найдена! Введите 'help' для получения списка команд.");
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
            if (command instanceof Promptable) {
                if (tokens.length > 1) {
                    ((Promptable) command).execute(reader, tokens[1]);
                } else {
                    ((Promptable) command).execute(reader, null);
                }
            }
            else {
                if (tokens.length > 1) {
                    command.execute(tokens[1]);
                } else {
                    command.execute(null);
                }
            }
        }
        else System.err.println("Команда не найдена! Введите 'help' для получения списка команд.");
    }
}


