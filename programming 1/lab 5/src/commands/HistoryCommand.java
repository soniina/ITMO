package commands;

import java.util.*;

/**
 * Команда для вывода истории команд.
 */

public class HistoryCommand extends Command {
    public List<String> history;
    public HistoryCommand(List<String> history) {
        super("history", "последние команды");
        this.history = history;
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else {
            Iterator<String> it = history.iterator();
            for (int i = 0; i < Math.max(0, history.size() - 14); i++) { it.next(); }
            while (it.hasNext()) {
                System.out.println(it.next());
            }
            System.out.println("\n*все команды находятся в файле history");
        }
    }
}
