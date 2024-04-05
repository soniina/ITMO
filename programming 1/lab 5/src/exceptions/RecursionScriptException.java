package exceptions;

/**
 * Исключение, выбрасываемое при обнаружении рекурсивного вызова скрипта.
 */
public class RecursionScriptException extends RuntimeException {
    public RecursionScriptException() {
        super();
    }
}
