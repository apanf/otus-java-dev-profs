package homework.exception;

/**
 * Ошибка операций с наличностью.
 */
public class CurrencyException extends AtmException {
    public CurrencyException(String message) {
        super(message);
    }
}
