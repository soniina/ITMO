package commands;

import java.util.Map;

/**
 * Команда для вывода списка доступных команд с их описанием.
 */
public class HelpCommand extends Command{
    Map<String, Command> commands;
    public HelpCommand(Map<String, Command> commands) {
        super("help", "помощь");
        this.commands = commands;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else {
            System.out.println("Список доступных команд:");
            for (Command command : commands.values()) {
                System.out.println(command.getName() + " - " + command.getDescr());
            }
        }
    }
}
