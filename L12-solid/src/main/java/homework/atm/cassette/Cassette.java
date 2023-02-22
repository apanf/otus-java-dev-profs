package homework.atm.cassette;

import homework.currency.Banknote;

public interface Cassette extends Comparable<Cassette> {
    Banknote getBanknote();

    int getRemainder();

    int reduceCashAmount(int amount);

    @Override
    default int compareTo(Cassette o) {
        return Integer.compare(getBanknote().getDenomination(), o.getBanknote().getDenomination());
    }
}
