package exceptions;

/**
 * Исключение, выбрасываемое при неудачной попытке получить константу перечисления.
 */
public class EnumConstantNotFoundException extends RuntimeException {
    public EnumConstantNotFoundException(String message) {
        super(message);
    }
}
