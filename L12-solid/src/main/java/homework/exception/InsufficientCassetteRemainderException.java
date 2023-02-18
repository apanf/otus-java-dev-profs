package homework.exception;

/**
 * Недостаток наличных в кассете.
 */
public class InsufficientCassetteRemainderException extends WithdrawalCashException {
    public InsufficientCassetteRemainderException(String message) {
        super(message);
    }
}