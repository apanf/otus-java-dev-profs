package homework.exception;

/**
 * Общие ошибки расчёта наличных средств к выдаче.
 */
public class WithdrawalCashException extends CurrencyException {
    public WithdrawalCashException(String format, Object... args) {
        super(format, args);
    }
}
