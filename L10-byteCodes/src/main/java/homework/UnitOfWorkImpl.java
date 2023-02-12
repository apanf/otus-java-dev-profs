package homework;

import homework.annotations.Log;
import homework.annotations.Meter;
import homework.annotations.Save;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
public class UnitOfWorkImpl implements UnitOfWork<String> {
    @Log
    @Meter
    @Override
    public void doWork() {
        log.info("Нет работы, печатаю время: {}", LocalDateTime.now());
    }

    @Override
    public void doWork(String o) {
        log.info("Принят аргумент: {}", o);
    }

    @Override
    @Meter
    @Log
    public void doWork(String... args) {
        log.info("Количество принятых аргументов {}", args.length);
        Arrays.stream(args)
                .forEach(a -> log.info("Принят аргумент: {}", a));
    }

    @Log
    @Meter
    @Save
    @Override
    public void doWork(int first, int last, String... args) {
        log.info("Количество принятых аргументов {}, обработка аргументов с {} по {}", args.length, first, last);
        Arrays.stream(args)
                .skip(first)
                .limit(last)
                .forEach(a -> log.info("Принят аргумент: {}", a));
    }
}
