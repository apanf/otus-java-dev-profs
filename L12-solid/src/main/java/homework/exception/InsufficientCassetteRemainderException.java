package homework.exception;

/**
 * Недостаток наличных в кассете.
 */
public class InsufficientCassetteRemainderException extends WithdrawalCashException {
    public InsufficientCassetteRemainderException(String format, Object... args) {
        super(format, args);
    }
}