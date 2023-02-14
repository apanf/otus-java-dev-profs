package homework;

import homework.annotations.AnnotationHandler;
import homework.annotations.Log;
import homework.annotations.LogAnnotationHandler;
import homework.annotations.Meter;
import homework.annotations.MeterAnnotationHandler;
import homework.annotations.Save;
import homework.annotations.SaveAnnotationHandler;
import homework.ioc.IocVer1;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Map<Class<? extends Annotation>, AnnotationHandler> annotationHandlers = new HashMap<>();
        annotationHandlers.put(Log.class, new LogAnnotationHandler());
        annotationHandlers.put(Meter.class, new MeterAnnotationHandler());
        annotationHandlers.put(Save.class, new SaveAnnotationHandler());

        IocVer1 ioc = new IocVer1(annotationHandlers);

        var unitOfWork = (UnitOfWork<String>) ioc.createClass(UnitOfWorkImpl.class);
        unitOfWork.doWork(1, 3, "Привет", "Пока", "Cheers");
    }
}
