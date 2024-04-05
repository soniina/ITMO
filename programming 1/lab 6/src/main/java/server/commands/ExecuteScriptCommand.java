package server.commands;


import common.communication.Response;
import common.communication.ResponseStatus;
import server.managers.CommandManager;
import server.exceptions.*;

import java.io.*;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Команда для исполнения скрипта из файла.
 */
public class ExecuteScriptCommand extends Command {
    private final CommandManager commandManager;
    private static Stack<String> scriptStack;
    public ExecuteScriptCommand(CommandManager commandManager) {
        super("execute_script file_name", "исполнить скрипт из файла");
        scriptStack = new Stack<>();
        this.commandManager = commandManager;
    }

    /**
     * Исполняет команды из указанного файла.
     * @param file файл скрипта.
     */
    @Override
    public Response execute(String file) {
        if (file == null) throw new IllegalArgumentException("Команда execute_script требует аргумент file_name\n");
        else {
            if (file.equals("history1") || file.equals("history2") || file.equals("history3")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(file + "_copy"))) {
                    String line;
                    String previousLine = null;
                    while ((line = reader.readLine()) != null) {
                        if (previousLine != null) {
                            writer.write(previousLine);
                            writer.newLine();
                        }
                        previousLine = line;
                    }
                    file += "_copy";
                } catch (IOException e) {
                    return new Response(ResponseStatus.ERROR,"Ошибка ввода-вывода: " + e.getMessage());
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                if ((!(scriptStack.isEmpty()) && (scriptStack.peek().equals(file))) || scriptStack.contains(file))
                    throw new RecursionScriptException();
                scriptStack.push(file);
                while ((line = reader.readLine()) != null) {
                    commandManager.execute(line, reader);
                }
                if (scriptStack.peek().equals(file)) scriptStack.pop();
                else throw new RecursionScriptException();
                return new Response(ResponseStatus.SUCCESS, "Скрипт успешно выполнен!");
            } catch (FileNotFoundException e) {
                return new Response(ResponseStatus.ERROR, String.format("Файл %s не найден =(\n", file));
            } catch (SecurityException e) {
                return new Response(ResponseStatus.ERROR,"Нет прав доступа к файлу " + file + ": " + e.getMessage());
            } catch (IOException e) {
                return new Response(ResponseStatus.ERROR,"Ошибка ввода-вывода: " + e.getMessage());
            } catch (EmptyStackException | RecursionScriptException e) {
                return new Response(ResponseStatus.ERROR, "Рекурсивный скрипт!");
            } finally {
                if (file.equals("history1_copy") || file.equals("history2_copy") || file.equals("history3_copy"))
                    new File(file).delete();
            }
        }
    }
}

