package homework.atm.logic;

import homework.atm.cassette.Cassette;
import homework.currency.Banknote;
import homework.currency.CurrencyName;

import java.util.Map;

public interface Logic {
    CurrencyName getCurrencyName();

    Map<Banknote, Integer> calculateCashForWithdrawalPlan(Map<Banknote, Cassette> atmCassettes, int amount);
}
