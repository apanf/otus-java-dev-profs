package homework.atm;

import homework.atm.cassette.Cassette;
import homework.atm.logic.Logic;
import homework.currency.Banknote;
import homework.currency.CurrencyName;
import homework.exception.CurrencyException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Atm {
    private final Map<CurrencyName, Logic> logics;
    private final Map<CurrencyName, Map<Banknote, Cassette>> cassettes = new HashMap<>();
    private final Set<CurrencyName> supportedCurrencies;

    public Atm(Map<CurrencyName, Logic> logics) {
        this.logics = logics;
        this.supportedCurrencies = logics.keySet();
        for (var each : supportedCurrencies)
            cassettes.put(each, new HashMap<>());
    }

    public Set<CurrencyName> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public void setLogic(Logic logic) {
        checkSupportedType(logic.getCurrencyName(), "Банкомат не поддерживает работу с данной валютой.");
        this.logics.put(logic.getCurrencyName(), logic);
    }

    public void addCassettes(List<Cassette> cassettes) {
        checkSupportCassetteType(cassettes);
        for (var each : cassettes) {
            Banknote banknote = each.getBanknote();

            this.cassettes.get(banknote.getName()).put(banknote, each);
        }
    }

    public void addCassettes(Cassette cassette) {
        Banknote banknote = cassette.getBanknote();

        checkSupportCassetteType(List.of(cassette));
        cassettes.get(banknote.getName()).put(banknote, cassette);
    }

    public void checkSupportCassetteType(List<Cassette> cassettes) {
        for (var each : cassettes) {
            CurrencyName curName = each.getBanknote().getName();

            checkSupportedType(curName, "Банкомат не поддерживает работу с этим типом валюты: " + curName);
        }
    }

    public Map<CurrencyName, Map<Banknote, Cassette>> getCassettes() {
        return Map.copyOf(cassettes);
    }

    public Map<Banknote, Integer> getCash(int amount) {
        if (supportedCurrencies.size() != 1)
            throw new CurrencyException("Укажите тип валюты. Банкомат поддерживает: " + supportedCurrencies);
        return getCash(supportedCurrencies.iterator().next(), amount);
    }

    public Map<Banknote, Integer> getCash(CurrencyName currencyName, int amount) {
        checkSupportedType(currencyName, "Банкомат не работает с этой валюбой: " + currencyName);

        Map<Banknote, Integer> withdrawalPlan = logics.get(currencyName)
                .calculateCashForWithdrawalPlan(Map.copyOf(cassettes.get(currencyName)), amount);

        return recalculateCassetteCashRemainder(currencyName, withdrawalPlan);
    }

    private void checkSupportedType(CurrencyName currencyName, String msg) {
        if (!supportedCurrencies.contains(currencyName))
            throw new CurrencyException(msg);
    }

    private Map<Banknote, Integer> recalculateCassetteCashRemainder(CurrencyName currencyName, Map<Banknote, Integer> cashMap) {
        for (var each : cashMap.entrySet())
            cassettes.get(currencyName).get(each.getKey()).reduceCashAmount(each.getValue());
        return cashMap;
    }
}