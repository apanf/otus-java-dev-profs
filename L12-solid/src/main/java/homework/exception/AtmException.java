package homework.exception;

/**
 * Общий класс исключений банкомата.
 */
public class AtmException extends RuntimeException {
    public AtmException(String message) {
        super(message);
    }
}
