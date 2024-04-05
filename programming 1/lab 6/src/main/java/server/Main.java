package server;

import common.communication.RequestCommand;
import common.communication.Response;
import server.commands.SaveCommand;
import server.network.Server;
import common.models.StudyGroup;
import server.managers.*;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Основной класс приложения, отвечающий за запуск серверной части программы.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        PriorityQueue<StudyGroup> collection = new PriorityQueue<>();
        String loadFile = null;
        if (args.length > 0) loadFile = args[0];

        Server server = new Server();

        CollectionManager collectionManager = new CollectionManager(collection, loadFile, server);
        CommandManager commandManager = new CommandManager(collectionManager, server);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            RequestCommand requestCommand = (RequestCommand) server.readRequest();
            Response response = commandManager.execute(requestCommand);
            server.sendResponse(response);
            if (System.in.available() > 0) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] tokens = line.split(" ");
                    String arg = null;
                    if (tokens.length > 1) arg = tokens[1];
                    try {
                        if (tokens[0].equals("save")) new SaveCommand(collectionManager).execute(arg);
                        else System.err.println("Недоступная команда!");
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }
}