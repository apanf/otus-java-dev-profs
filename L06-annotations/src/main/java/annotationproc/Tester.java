package annotationproc;

import annotationproc.annotation.After;
import annotationproc.annotation.Before;
import annotationproc.annotation.Test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static annotationproc.Tester.StageType.AFTER;
import static annotationproc.Tester.StageType.BEFORE;
import static annotationproc.Tester.StageType.TEST;

public class Tester {
    @AllArgsConstructor
    @Getter
    public enum StageType {
        BEFORE(Before.class), TEST(Test.class), AFTER(After.class);
        private final Class<? extends Annotation> type;
    }

    public Map<StageType, Pair<int[], List<String>>> execute(String className) {
        try {
            return execute(Class.forName(className));
        } catch (Exception e) {
            throw new IllegalStateException("Не удаётся получить ссылку на класс " + className);
        }
    }

    public Map<StageType, Pair<int[], List<String>>> execute(Class<?> clazz) {
        var beforeMethods = getAnnotatedMethods(clazz, BEFORE);
        var testMethods = getAnnotatedMethods(clazz, TEST);
        var afterMethods = getAnnotatedMethods(clazz, AFTER);
        var statistics = createStatisticsInstance(beforeMethods.length, testMethods.length,
                afterMethods.length);

        for (Method each : testMethods) {
            Object instance = getInstance(clazz);

            try {
                executeSetUp(instance, beforeMethods, each, statistics.get(BEFORE));
                executeTestMethod(instance, each, statistics.get(TEST));
            } catch (Exception e) {
                // nop
            } finally {
                executeTearDown(instance, afterMethods, each, statistics.get(AFTER));
            }
        }
        printStatistics(statistics);
        return new HashMap<>(statistics);
    }

    private Map<StageType, Pair<int[], List<String>>> createStatisticsInstance(int before, int test, int after) {
        /*
        StageType - фаза тестирования.
        int[2] - счётчики (всего, удачно).
        List<String> - список ошибок.
         */
        Map<StageType, Pair<int[], List<String>>> result = new HashMap<>();

        result.put(BEFORE, Pair.of(new int[]{test == 0 ? before : test * before, 0}, new ArrayList<>()));
        result.put(TEST, Pair.of(new int[]{test, 0}, new ArrayList<>()));
        result.put(AFTER, Pair.of(new int[]{test == 0 ? after : test * after, 0}, new ArrayList<>()));
        return result;
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException
                    ("Для создания экзепляра тестового класса требуется конструктор без аргументов.");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void executeSetUp(Object instance, Method[] methods, Method currTestMethod,
                              Pair<int[], List<String>> statsPair) {
        for (Method each : methods) {
            try {
                each.invoke(instance);
                statsPair.getLeft()[1]++;
            } catch (Exception e) {
                statsPair.getRight().add(generateErrorMessage(each, currTestMethod, e));
                throw new RuntimeException();
            }
        }
    }

    private void executeTestMethod(Object instance, Method method, Pair<int[], List<String>> statsPair) {
        try {
            method.invoke(instance);
            statsPair.getLeft()[1]++;
        } catch (Exception e) {
            statsPair.getRight().add(generateErrorMessage(method, method, e));
        }
    }

    private void executeTearDown(Object instance, Method[] methods, Method currTestMethod,
                                 Pair<int[], List<String>> statsPair) {
        for (Method each : methods) {
            try {
                each.invoke(instance);
                statsPair.getLeft()[1]++;
            } catch (Exception e) {
                statsPair.getRight().add(generateErrorMessage(each, currTestMethod, e));
            }
        }
    }

    private String generateErrorMessage(Method currExecMethod, Method currTestMethod, Exception e) {
        StringBuilder sb = new StringBuilder();

        sb.append("Тестовый метод: ").append(currTestMethod.getName()).append(". ");
        if (!currTestMethod.equals(currExecMethod))
            sb.append("Выполняемый метод: ").append(currExecMethod.getName()).append(". ");
        sb.append("Исключение: ").append(e.getClass().getSimpleName()).append(". ");
        sb.append("Сообщение: ").append(e.getMessage()).append(".");
        return sb.toString();
    }

    private Method[] getAnnotatedMethods(Class<?> clazz, StageType stage) {
        Method[] result;

        try {
            result = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(stage.type))
                    .toArray(Method[]::new);
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось получить методы аннотированные " + stage.type);
        }
        return result;
    }

    private void printStatistics(Map<StageType, Pair<int[], List<String>>> statistics) {
        var testStat = statistics.get(TEST);

        if (testStat.getLeft()[0] == 0) {
            System.out.println("Нет тестов для выполнения.");
            return;
        }
        System.out.println("Статистика выполнения:");
        printStageStatistics(statistics.get(BEFORE), "Методы инициализации \"Before\":");
        printStageStatistics(testStat, "Тесты \"Test\":");
        printStageStatistics(statistics.get(AFTER), "Методы завершения \"After\":");
    }

    private void printStageStatistics(Pair<int[], List<String>> currStats, String msg) {
        System.out.println(msg);
        if (currStats.getLeft()[0] == 0)
            System.out.format("%3sНет методов для запуска.\n", " ");
        else
            printCounters(currStats);
    }

    private void printCounters(Pair<int[], List<String>> statsPair) {
        int st;
        var errList = statsPair.getRight();

        System.out.format("%3sВсего: %d.\n", " ", statsPair.getLeft()[0]);
        System.out.format("%3sУспешно: %d.\n", " ", st = statsPair.getLeft()[1]);
        if (st == 0 && errList.isEmpty())
            System.out.format("%6sТесты не запускались, ошибка в методе инициализации (Before).\n", " ");
        if (!errList.isEmpty()) {
            System.out.format("%3sОшибок: %d\n", " ", errList.size());
            for (String each : errList)
                System.out.format("%6s%s.\n", " ", each);
        }
    }
}