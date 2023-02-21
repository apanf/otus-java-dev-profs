package homework.atm;

import homework.atm.cassette.CassetteImpl;
import homework.atm.logic.RubLogic;
import homework.exception.AtmException;
import homework.exception.InsufficientCassetteRemainderException;
import homework.exception.WithdrawalCashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static homework.currency.CurrencyName.RUB;
import static homework.currency.RubBanknotes.FIVE_HUNDRED;
import static homework.currency.RubBanknotes.FIVE_THOUSAND;
import static homework.currency.RubBanknotes.ONE_HUNDRED;
import static homework.currency.RubBanknotes.ONE_THOUSAND;
import static homework.currency.RubBanknotes.TWO_HUNDRED;
import static homework.currency.RubBanknotes.TWO_THOUSAND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmTest {
    private Atm atm;

    @BeforeEach
    public void setUp() {
        atm = new AtmImpl(Map.of(RUB, new RubLogic()));
    }

    @Test
    @DisplayName("Выдать все запрошенный купюры. Все номиналы купюр в наличии.")
    public void test1() {
        atm.addCassettes(List.of(
                new CassetteImpl(FIVE_THOUSAND, 2000),
                new CassetteImpl(TWO_THOUSAND, 2000),
                new CassetteImpl(ONE_THOUSAND, 2000),
                new CassetteImpl(FIVE_HUNDRED, 2000),
                new CassetteImpl(TWO_HUNDRED, 2000),
                new CassetteImpl(ONE_HUNDRED, 2000))
        );

        assertAll(
                () -> assertEquals(Map.of(ONE_HUNDRED, 1),
                        atm.getCash(100)),
                () -> assertEquals(Map.of(TWO_HUNDRED, 1),
                        atm.getCash(200)),
                () -> assertEquals(Map.of(FIVE_HUNDRED, 1, TWO_HUNDRED, 1, ONE_HUNDRED, 1),
                        atm.getCash(800)),
                () -> assertEquals(Map.of(FIVE_HUNDRED, 1, TWO_HUNDRED, 2),
                        atm.getCash(900)),
                () -> assertEquals(Map.of(ONE_THOUSAND, 1, ONE_HUNDRED, 1),
                        atm.getCash(1100)),
                () -> assertEquals(Map.of(TWO_THOUSAND, 2, TWO_HUNDRED, 2),
                        atm.getCash(4400)),
                () -> assertEquals(Map.of(FIVE_THOUSAND, 1, ONE_THOUSAND, 1, FIVE_HUNDRED, 1, ONE_HUNDRED, 1),
                        atm.getCash(6600)),
                () -> assertEquals(Map.of(FIVE_THOUSAND, 3, TWO_THOUSAND, 2, TWO_HUNDRED, 1, ONE_HUNDRED, 1),
                        atm.getCash(19300))
        );
    }

    @Test
    @DisplayName("Выдать все запрошенный купюры. Часть номиналов в наличии.")
    public void test2() {
        atm.addCassettes(List.of(
                new CassetteImpl(FIVE_THOUSAND, 2000),
                new CassetteImpl(ONE_THOUSAND, 2000),
                new CassetteImpl(ONE_HUNDRED, 2000))
        );

        assertAll(
                () -> assertEquals(Map.of(ONE_HUNDRED, 1),
                        atm.getCash(100)),
                () -> assertEquals(Map.of(ONE_HUNDRED, 2),
                        atm.getCash(200)),
                () -> assertEquals(Map.of(ONE_HUNDRED, 8),
                        atm.getCash(800)),
                () -> assertEquals(Map.of(ONE_HUNDRED, 9),
                        atm.getCash(900)),
                () -> assertEquals(Map.of(ONE_THOUSAND, 1, ONE_HUNDRED, 1),
                        atm.getCash(1100)),
                () -> assertEquals(Map.of(ONE_THOUSAND, 4, ONE_HUNDRED, 4),
                        atm.getCash(4400)),
                () -> assertEquals(Map.of(FIVE_THOUSAND, 1, ONE_THOUSAND, 1, ONE_HUNDRED, 6),
                        atm.getCash(6600)),
                () -> assertEquals(Map.of(FIVE_THOUSAND, 3, ONE_THOUSAND, 4, ONE_HUNDRED, 3),
                        atm.getCash(19300))
        );
    }

    @Test
    @DisplayName("Ошибка, в банкомат не вставлены кассеты.")
    public void test3() {
        assertThrows(AtmException.class, () -> atm.getCash(60));
    }

    @Test
    @DisplayName("Ошибка, запрошена некорректная сумма.")
    public void test4() {
        atm.addCassettes(new CassetteImpl(ONE_HUNDRED, 10));

        assertAll(
                () -> assertThrows(WithdrawalCashException.class, () -> atm.getCash(0)),
                () -> assertThrows(WithdrawalCashException.class, () -> atm.getCash(-100))
        );
    }

    @Test
    @DisplayName("Ошибка. Запрошена сумма некратная минимальному номиналу в наличии. Минимальный номинал в наличии 100.")
    public void test5() {
        atm.addCassettes(new CassetteImpl(ONE_HUNDRED, 10));

        assertAll(
                () -> assertThrows(WithdrawalCashException.class, () -> atm.getCash(60)),
                () -> assertThrows(WithdrawalCashException.class, () -> atm.getCash(120))
        );
    }

    @Test
    @DisplayName("Ошибка. Запрошена сумма некратная минимальному номиналу в наличии. Минимальный номинал в наличии 200.")
    public void test6() {
        atm.addCassettes(List.of(new CassetteImpl(TWO_HUNDRED, 10),
                new CassetteImpl(TWO_THOUSAND, 10)));

        assertAll(
                () -> assertThrows(WithdrawalCashException.class, () -> atm.getCash(100)),
                () -> assertThrows(WithdrawalCashException.class, () -> atm.getCash(300))
        );
    }

    @Test
    @DisplayName("Ошибка. Нет достаточного количества наличных для выдачи запрошенной суммы.")
    public void test7() {
        atm.addCassettes(List.of(
                new CassetteImpl(ONE_HUNDRED, 1),
                new CassetteImpl(TWO_HUNDRED, 1),
                new CassetteImpl(FIVE_HUNDRED, 1),
                new CassetteImpl(ONE_THOUSAND, 1),
                new CassetteImpl(TWO_THOUSAND, 1))
        );

        assertAll(
                () -> assertThrows(InsufficientCassetteRemainderException.class, () -> atm.getCash(400)),
                () -> assertThrows(InsufficientCassetteRemainderException.class, () -> atm.getCash(3900)),
                () -> assertThrows(InsufficientCassetteRemainderException.class, () -> atm.getCash(4000))
        );
    }

    @Test
    @DisplayName("Выдать валидную сумму наличных. Остаток в кассете должен быть уменьшен на сумму выданого.")
    public void test8() {
        int ir = 100;
        atm.addCassettes(List.of(
                new CassetteImpl(FIVE_THOUSAND, ir),
                new CassetteImpl(TWO_THOUSAND, ir),
                new CassetteImpl(ONE_THOUSAND, ir),
                new CassetteImpl(FIVE_HUNDRED, ir),
                new CassetteImpl(TWO_HUNDRED, ir),
                new CassetteImpl(ONE_HUNDRED, ir))
        );

        atm.getCash(19900);
        var cassettes = atm.getCassettes();

        assertAll(
                () -> assertEquals(cassettes.get(RUB).get(FIVE_THOUSAND).getRemainder(), ir - 3),
                () -> assertEquals(cassettes.get(RUB).get(TWO_THOUSAND).getRemainder(), ir - 2),
                () -> assertEquals(cassettes.get(RUB).get(ONE_THOUSAND).getRemainder(), ir),
                () -> assertEquals(cassettes.get(RUB).get(FIVE_HUNDRED).getRemainder(), ir - 1),
                () -> assertEquals(cassettes.get(RUB).get(TWO_HUNDRED).getRemainder(), ir - 2),
                () -> assertEquals(cassettes.get(RUB).get(ONE_HUNDRED).getRemainder(), ir)
        );
    }

    @Test
    @DisplayName("Ошибка. Запрошенная сумма не может быть выдана. Остаток наличных в кассетах не изменился.")
    public void test9() {
        int ir = 2;
        atm.addCassettes(List.of(
                new CassetteImpl(FIVE_THOUSAND, ir),
                new CassetteImpl(TWO_THOUSAND, ir),
                new CassetteImpl(ONE_THOUSAND, ir),
                new CassetteImpl(FIVE_HUNDRED, ir),
                new CassetteImpl(TWO_HUNDRED, ir),
                new CassetteImpl(ONE_HUNDRED, ir))
        );

        assertThrows(InsufficientCassetteRemainderException.class, () -> atm.getCash(19900));
        var cassettes = atm.getCassettes();

        assertAll(
                () -> assertEquals(cassettes.get(RUB).get(FIVE_THOUSAND).getRemainder(), ir),
                () -> assertEquals(cassettes.get(RUB).get(TWO_THOUSAND).getRemainder(), ir),
                () -> assertEquals(cassettes.get(RUB).get(ONE_THOUSAND).getRemainder(), ir),
                () -> assertEquals(cassettes.get(RUB).get(FIVE_HUNDRED).getRemainder(), ir),
                () -> assertEquals(cassettes.get(RUB).get(TWO_HUNDRED).getRemainder(), ir),
                () -> assertEquals(cassettes.get(RUB).get(ONE_HUNDRED).getRemainder(), ir)
        );
    }
}