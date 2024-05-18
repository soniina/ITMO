package common.communication;

import java.io.Serializable;

/**
 * Класс команды запроса, отправляемой от клиента к серверу.
 */
public record RequestCommand(String name, String arg) implements Serializable {}
