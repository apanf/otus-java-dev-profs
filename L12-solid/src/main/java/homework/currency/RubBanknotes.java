package homework.currency;

import lombok.AllArgsConstructor;

import static homework.currency.CurrencyName.RUB;

@AllArgsConstructor
public enum RubBanknotes implements Banknote {
    FIVE_THOUSAND(5000),
    TWO_THOUSAND(2000),
    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500),
    TWO_HUNDRED(200),
    ONE_HUNDRED(100),
    FIFTY(50);
    private final int denomination;
    private final static CurrencyName NAME = RUB;

    @Override
    public int getDenomination() {
        return denomination;
    }

    @Override
    public CurrencyName getName() {
        return NAME;
    }
}