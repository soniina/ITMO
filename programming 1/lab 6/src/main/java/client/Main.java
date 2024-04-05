package client;

import client.managers.ResponseManager;
import client.network.Client;
import java.util.Scanner;

import common.communication.*;

/**
 * Основной класс приложения, отвечающий за запуск клиентской части программы.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Client client;
        client = new Client();
        ResponseManager responseManager = new ResponseManager(client);

        while (sc.hasNext()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                String[] tokens = line.split(" ");
                String arg = null;
                if (tokens.length > 1) arg = tokens[1];
                RequestCommand request = new RequestCommand(tokens[0], arg);
                client.sendRequest(request);
                Response response = null;
                while (response == null || (response.getStatus() != ResponseStatus.SUCCESS && response.getStatus() != ResponseStatus.ERROR)) {
                    response = client.readResponse();
                    responseManager.handle(response);
                }
            }
        }
        sc.close();
        client.close();
    }
}
