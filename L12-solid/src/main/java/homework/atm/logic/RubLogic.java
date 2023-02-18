package homework.atm.logic;

import homework.atm.cassette.Cassette;
import homework.currency.Banknote;
import homework.currency.CurrencyName;

import java.util.Map;

public class RubLogic extends AbstractLogic {
    @Override
    public CurrencyName getCurrencyName() {
        return CurrencyName.RUB;
    }

    @Override
    public Map<Banknote, Integer> calculateCashForWithdrawalPlan(Map<Banknote, Cassette> atmCassettes, int requestAmount) {
        return super.calculateCashForWithdrawalPlan(atmCassettes, requestAmount);
    }
}