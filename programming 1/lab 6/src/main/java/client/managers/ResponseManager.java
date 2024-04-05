package client.managers;

import client.network.Client;
import common.communication.Response;
import common.utils.ObjectBuilder;

/**
 * Менеджер обработки ответов от сервера.
 */
public class ResponseManager {
    private final Client client;

    public ResponseManager(Client client) {
        this.client = client;
    }

    /**
     * Обрабатывает ответ от сервера.
     * @param response ответ сервера.
     */
    public void handle(Response response) {
        switch (response.getStatus()) {
            case SUCCESS, INTERMEDIATE_SUCCESS -> System.out.println(response.getDescr());
            case ERROR, INTERMEDIATE_ERROR, HISTORY_OVERFLOW -> System.err.println(response.getDescr());
            case ASK_OBJECT -> {
                client.sendRequest(ObjectBuilder.create());
            }
            case EXIT -> {
                System.out.println(response.getDescr());
                System.exit(0);
            }
            default -> System.out.println("не учла папапаараша");
        }
    }
}
