package ru.otus.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessorEvenSecondExceptionTest {

    Processor processor;

    @BeforeEach
    public void setUp() {
        processor = new ProcessorEvenSecondException();
    }

    @Test
    @DisplayName("Выбрасывание исключения каждую четную секунду.")
    public void test1() throws InterruptedException {
        Message actual = new Message.Builder(1L).build();
        long currentSeconds = System.currentTimeMillis() / 1000;
        long delay = currentSeconds % 2 == 0 ? 2 : 1;

        // Ждём начала следующей чётной скунды (+1 миллисекунда для надёжности).
        TimeUnit.MILLISECONDS.sleep(1000 * (currentSeconds + delay) - System.currentTimeMillis() + 1);

        assertTrue(LocalDateTime.now().getSecond() % 2 == 0);
        assertThrows(RuntimeException.class, () -> processor.process(actual));
    }

    @Test
    @DisplayName("Не выбрасывается исключение каждую нечетную секунду.")
    public void test2() throws InterruptedException {
        Message actual = new Message.Builder(1L).build();
        long currentSeconds = System.currentTimeMillis() / 1000;
        long delay = currentSeconds % 2 != 0 ? 2 : 1;

        TimeUnit.MILLISECONDS.sleep(1000 * (currentSeconds + delay) - System.currentTimeMillis() + 1);

        assertTrue(LocalDateTime.now().getSecond() % 2 != 0);
        assertDoesNotThrow(() -> processor.process(actual));
    }
}