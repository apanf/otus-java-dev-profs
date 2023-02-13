package homework;

import homework.annotations.AnnotationHandler;
import homework.annotations.Log;
import homework.annotations.LogAnnotationHandler;
import homework.annotations.Meter;
import homework.annotations.MeterAnnotationHandler;
import homework.annotations.Save;
import homework.annotations.SaveAnnotationHandler;
import homework.ioc.Ioc;
import homework.ioc.IocOpt;
import homework.ioc.IocVer1;
import homework.testClasses.UnitOfWorkDouble;
import homework.testClasses.UnitOfWorkTestImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unchecked")
@Slf4j
public class IocTest {
    private Ioc ioc;

    @BeforeEach
    public void setUp() {
        Map<Class<? extends Annotation>, AnnotationHandler> annotationHandlers = new HashMap<>();
        annotationHandlers.put(Log.class, new LogAnnotationHandler());
        annotationHandlers.put(Meter.class, new MeterAnnotationHandler());
        annotationHandlers.put(Save.class, new SaveAnnotationHandler());

        ioc = new IocOpt(annotationHandlers);
    }

    @Test
    @DisplayName("Проверка, что для одного и того же класса используется 1 обработчик")
    public void test1() {
        UnitOfWork<String> work1 = (UnitOfWork<String>) ioc.createClass(UnitOfWorkTestImpl.class);
        UnitOfWork<String> work2 = (UnitOfWork<String>) ioc.createClass(UnitOfWorkTestImpl.class);

        work1.doWork();
        work1.doWork("test");
        work2.doWork();

        assertEquals(1, ioc.getProxiedClasses().size());
    }

    @Test
    @DisplayName("Проверка, что для разных классов, используются разные обработчики")
    public void test2() {
        UnitOfWork<String> work1 = (UnitOfWork<String>) ioc.createClass(UnitOfWorkTestImpl.class);
        UnitOfWork<Double> work2 = (UnitOfWork<Double>) ioc.createClass(UnitOfWorkDouble.class);

        work1.doWork();
        work1.doWork("test");
        work2.doWork(1.2, 2.4, 3.1, 4.4, 5.4, 6.0, 0.7);
        work1.doWork();

        assertEquals(2, ioc.getProxiedClasses().size());
    }

    @Test
    @Disabled
    @DisplayName("Тестирование скорости работы разных реализаций Ioc.")
    public void test3() {
        Map<Class<? extends Annotation>, AnnotationHandler> annotationHandlers = new HashMap<>();
        Ioc iocVer1 = new IocVer1(annotationHandlers);
        long counterOpt;
        long counterVer1;
        UnitOfWork<Double> proxyOpt = (UnitOfWork<Double>) ioc.createClass(UnitOfWorkDouble.class);
        UnitOfWork<Double> proxyVer1 = (UnitOfWork<Double>) iocVer1.createClass(UnitOfWorkDouble.class);

        counterVer1 = System.currentTimeMillis();
        test3_performanceTest(proxyVer1);
        counterVer1 = System.currentTimeMillis() - counterVer1;

        counterOpt = System.currentTimeMillis();
        test3_performanceTest(proxyOpt);
        counterOpt = System.currentTimeMillis() - counterOpt;

        log.info("Время выполнения теста для IocVer1, мс: {}", counterVer1);
        log.info("Время выполнения теста для IocOpt, мс: {}", counterOpt);
        log.info("Прирост производительности для IocOpt, раз: {}", counterVer1 / counterOpt);
    }

    private void test3_performanceTest(UnitOfWork<Double> proxy) {
        for (int i = 0; i < 10_000_000; i++)
            proxy.doWork(0.1);
    }
}
