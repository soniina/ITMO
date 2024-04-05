package commands;

import exceptions.RecursionScriptException;
import managers.CommandManager;

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
    public void execute(String file) {
        if (file == null) System.err.println("Команда execute_script требует аргумент file_name");
        else {
            if (file.equals("history")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file));
                     BufferedWriter writer = new BufferedWriter(new FileWriter("history_copy"))) {
                    String line;
                    String previousLine = null;
                    while ((line = reader.readLine()) != null) {
                        if (previousLine != null) {
                            writer.write(previousLine);
                            writer.newLine();
                        }
                        previousLine = line;
                    }
                    file = "history_copy";
                } catch (IOException e) {
                    System.err.println("Ошибка ввода-вывода: " + e.getMessage());
                    return;
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                if ((!(scriptStack.isEmpty()) && (scriptStack.peek().equals(file))) || scriptStack.contains(file)) throw new RecursionScriptException();
                scriptStack.push(file);
                while ((line = reader.readLine()) != null) {
                    commandManager.execute(line, reader);
                }
                if (scriptStack.peek().equals(file)) scriptStack.pop();
                else throw new RecursionScriptException();
            } catch (FileNotFoundException e) {
                System.err.printf("Файл %s не найден =(\n", file);
            } catch (SecurityException e) {
                System.err.println("Нет прав доступа к файлу " + file + ": " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            } catch (EmptyStackException | RecursionScriptException e) {
                System.err.println("Рекурсивный скрипт!");
            } finally {
                new File("history_copy").delete();
            }
        }
    }
}

