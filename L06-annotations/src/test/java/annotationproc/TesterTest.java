package annotationproc;

import annotationproc.testclasses.TestClass1;
import annotationproc.testclasses.TestClass3;
import annotationproc.testclasses.TestClass4;
import annotationproc.testclasses.TestClass5;
import annotationproc.testclasses.TestClass6;
import annotationproc.testclasses.TestClass7;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static annotationproc.Tester.StageType.AFTER;
import static annotationproc.Tester.StageType.BEFORE;
import static annotationproc.Tester.StageType.TEST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TesterTest {
    private Tester tester;

    @BeforeEach
    public void setUp() {
        tester = new Tester();
    }

    @Test
    public void verifyTestClass1() {
        var stats = tester.execute(TestClass1.class);

        assertAll(
                () -> assertEquals(4, stats.get(BEFORE).getLeft()[1]),
                () -> assertEquals(2, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(4, stats.get(AFTER).getLeft()[1])
        );
    }

    @Test
    public void verifyTestClass2() {
        var stats = tester.execute("annotationproc.testclasses.TestClass2");

        assertAll(
                () -> assertEquals(4, stats.get(BEFORE).getLeft()[0]),
                () -> assertTrue(stats.get(BEFORE).getLeft()[1] <= 2),
                () -> assertEquals(2, stats.get(TEST).getLeft()[0]),
                () -> assertEquals(0, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(4, stats.get(AFTER).getLeft()[0]),
                () -> assertEquals(2, stats.get(AFTER).getLeft()[1])
        );
    }

    @Test
    public void verifyTestClass3() {
        var stats = tester.execute(TestClass3.class);

        assertAll(
                () -> assertTrue(stats.get(BEFORE).getLeft()[1] <= 2),
                () -> assertEquals(0, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(0, stats.get(AFTER).getLeft()[1])
        );
    }

    @Test
    public void verifyTestClass4() {
        var stats = tester.execute(TestClass4.class);

        assertAll(
                () -> assertEquals(4, stats.get(BEFORE).getLeft()[1]),
                () -> assertEquals(1, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(2, stats.get(AFTER).getLeft()[1])
        );
    }

    @Test
    public void verifyTestClass5() {
        var stats = tester.execute(TestClass5.class);

        assertAll(
                () -> assertEquals(0, stats.get(BEFORE).getLeft()[1]),
                () -> assertEquals(0, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(0, stats.get(AFTER).getLeft()[1])
        );
    }

    @Test
    public void verifyTestClass6() {
        var stats = tester.execute(TestClass6.class);

        assertAll(
                () -> assertEquals(1, stats.get(BEFORE).getLeft()[1]),
                () -> assertEquals(1, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(0, stats.get(AFTER).getLeft()[1])
        );
    }

    @Test
    public void verifyTestClass7() {
        var stats = tester.execute(TestClass7.class);

        assertAll(
                () -> assertEquals(0, stats.get(BEFORE).getLeft()[1]),
                () -> assertEquals(2, stats.get(TEST).getLeft()[1]),
                () -> assertEquals(4, stats.get(AFTER).getLeft()[1])
        );
    }
}