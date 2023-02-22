package homework.atm.cassette;

import homework.currency.Banknote;
import homework.exception.CurrencyCassetteException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CassetteImpl implements Cassette {
    private final Banknote banknote;
    private int remainder;

    @Override
    public int getRemainder() {
        return remainder;
    }

    @Override
    public int reduceCashAmount(int amount) {
        if (amount > remainder)
            throw new CurrencyCassetteException("Кассета {} не содержит требуемой суммы.", this);
        return this.remainder -= amount;
    }

    @Override
    public String toString() {
        return "CassetteImpl{" +
                "currency name =" + banknote.getName() +
                ", denomination =" + banknote.getDenomination() +
                '}';
    }
}