package homework.exception;

/**
 * Ошибки кассеты.
 */
public class CurrencyCassetteException extends CurrencyException {
    public CurrencyCassetteException(String format, Object... args) {
        super(format, args);
    }
}
