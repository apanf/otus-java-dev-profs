package homework.atm;

import homework.atm.cassette.Cassette;
import homework.atm.logic.Logic;
import homework.currency.Banknote;
import homework.currency.CurrencyName;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Atm {
    Set<CurrencyName> getSupportedCurrencies();

    void setLogic(Logic logic);

    void addCassettes(List<Cassette> cassettes);

    void addCassettes(Cassette cassette);

    Map<CurrencyName, Map<Banknote, Cassette>> getCassettes();

    Map<Banknote, Integer> getCash(int amount);

    Map<Banknote, Integer> getCash(CurrencyName currencyName, int amount);
}
