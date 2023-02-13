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
import homework.testClasses.UnitOfWorkTestImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class AnnotationHandlersTest {
    @Spy
    private LogAnnotationHandler logAnnotationHandler;
    @Spy
    private SaveAnnotationHandler saveAnnotationHandler;
    @Spy
    private MeterAnnotationHandler meterAnnotationHandler;
    private Ioc ioc;

    @BeforeEach
    public void setUp() {
        Map<Class<? extends Annotation>, AnnotationHandler> annotationHandlers = new HashMap<>();
        annotationHandlers.put(Log.class, logAnnotationHandler);
        annotationHandlers.put(Save.class, saveAnnotationHandler);
        annotationHandlers.put(Meter.class, meterAnnotationHandler);

        ioc = new IocOpt(annotationHandlers);
    }

    @Test
    @DisplayName("Проверка работы аннотаций с фазой вызова BEFORE")
    public void test1() {
        UnitOfWork<String> work = (UnitOfWork<String>) ioc.createClass(UnitOfWorkTestImpl.class);

        work.doWork(1, 2, "1", "2", "3");

        verify(logAnnotationHandler).handle(any());
        verify(saveAnnotationHandler).handle(any());
    }

    @Test
    @DisplayName("Проверка работы аннотаций с фазой вызова AROUND")
    public void test2() {
        UnitOfWork<String> work = (UnitOfWork<String>) ioc.createClass(UnitOfWorkTestImpl.class);

        work.doWork(1, 2, "1", "2", "3");

        verify(meterAnnotationHandler, times(2)).handle(any());
    }
}