package exceptions;

public class NotEnoughCreaturesException extends RuntimeException{
    public NotEnoughCreaturesException(String message) {
        super(message);
    }
}
