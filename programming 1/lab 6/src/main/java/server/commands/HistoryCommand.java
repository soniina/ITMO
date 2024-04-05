package server.commands;

import common.communication.Response;
import common.communication.ResponseStatus;
import server.utils.HistoryWriter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Команда для вывода истории команд.
 */

public class HistoryCommand extends Command {
    public HistoryCommand() {
        super("history", "последние команды");
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public Response execute(String arg) {
        if (arg != null) throw new IllegalArgumentException(String.format("Команда %s не принимает аргументы\n", getName()));
        else {
            List<String> history = HistoryWriter.getCommandHistory();
            return new Response(ResponseStatus.SUCCESS,
                    history.stream().skip(Math.max(0, history.size() - 14)).collect(Collectors.joining("\n"))
                            + "\n\n*все команды находятся в файле " + HistoryWriter.getHistoryFile() + "\n");
//            String descr = "";
//            Iterator<String> it = history.iterator();
//            for (int i = 0; i < Math.max(0, history.size() - 14); i++) { it.next(); }
//            while (it.hasNext()) {
//                descr += it.next() + "\n";
//            }
//            return new Response(ResponseStatus.SUCCESS, descr + "*все команды находятся в файле history\n");
        }
    }
}
