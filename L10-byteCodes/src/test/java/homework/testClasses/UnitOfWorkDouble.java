package homework.testClasses;

import homework.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class UnitOfWorkDouble implements UnitOfWork<Double> {
    @Override
    public void doWork() {
        // nop
    }

    @Override
    public void doWork(Double o) {

    }

    @Override
    public void doWork(Double... args) {
        double sum = Arrays.stream(args).mapToDouble(l -> l).sum();

        log.info("Cумма принятых аргументов {}", sum);

    }

    @Override
    public void doWork(int first, int last, Double... args) {

    }
}
