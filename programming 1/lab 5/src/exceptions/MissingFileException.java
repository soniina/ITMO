package exceptions;

/**
 * Исключение, выбрасываемое при отсутствии требуемого файла.
 */
public class MissingFileException extends RuntimeException {
    public MissingFileException (String message) {
        super(message);
    }
}
