package homework.atm.logic;

import homework.atm.cassette.Cassette;
import homework.currency.Banknote;
import homework.exception.AtmException;
import homework.exception.InsufficientCassetteRemainderException;
import homework.exception.WithdrawalCashException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

abstract public class AbstractLogic implements Logic {
    public Map<Banknote, Integer> calculateCashForWithdrawalPlan(Map<Banknote, Cassette> atmCassettes, int requestAmount) {
        NavigableMap<Banknote, Cassette> cassettes = getSortedCassettesMap(atmCassettes);
        Banknote floorNominalBanknote;
        Map<Banknote, Integer> result;

        checkWithdrawalPossibility(cassettes, requestAmount);
        floorNominalBanknote = getFloorDenominationCassette(cassettes, requestAmount);
        result = new HashMap<>();
        if (resultCalculator(cassettes, requestAmount, floorNominalBanknote, result) != 0)
            throw new InsufficientCassetteRemainderException("Нет достаточного средств в банкомате для снятия запрошенной суммы");
        return result;
    }

    private NavigableMap<Banknote, Cassette> getSortedCassettesMap(Map<Banknote, Cassette> nonSortedCassettes) {
        NavigableMap<Banknote, Cassette> result = new TreeMap<>(Comparator.comparingInt(Banknote::getDenomination));

        result.putAll(nonSortedCassettes);
        return result;
    }

    private int resultCalculator(NavigableMap<Banknote, Cassette> cassettes,
                                 int remainder,
                                 Banknote lowerNominalBanknote,
                                 Map<Banknote, Integer> result) {
        if (remainder == 0 || lowerNominalBanknote == null)
            return remainder;

        int lowerBanknoteDenomination = lowerNominalBanknote.getDenomination();
        int requestBanknoteNum = remainder / lowerBanknoteDenomination;
        int curCassetteBanknoteNum = cassettes.get(lowerNominalBanknote).getRemainder();
        int currBanknoteNum = Math.min(curCassetteBanknoteNum, requestBanknoteNum);

        if (requestBanknoteNum != 0)
            result.put(lowerNominalBanknote, currBanknoteNum);
        remainder -= currBanknoteNum * lowerBanknoteDenomination;
        lowerNominalBanknote = cassettes.lowerKey(lowerNominalBanknote);
        return resultCalculator(cassettes, remainder, lowerNominalBanknote, result);
    }

    private Banknote getFloorDenominationCassette(NavigableMap<Banknote, Cassette> cassettes, int requestAmount) {
        for (var each : cassettes.descendingKeySet())
            if (each.getDenomination() <= requestAmount)
                return each;
        throw new WithdrawalCashException("Запрошенная сумма не может быть выдана. Сумма к снятию должна быть кратна "
                + cassettes.firstKey());
    }

    private void checkWithdrawalPossibility(SortedMap<Banknote, Cassette> cassettes, int requestAmount) {
        if (cassettes.isEmpty())
            throw new AtmException("Нет выдающих кассет.");

        int minDenomination = cassettes.firstKey().getDenomination();

        if (requestAmount < 1)
            throw new WithdrawalCashException("Запрошенная сумма должна быть положительна");
        if (requestAmount % minDenomination != 0)
            throw new WithdrawalCashException("Запрошенная сумма не может быть выдана. Сумма к снятию должна быть кратна "
                    + minDenomination);
    }
}