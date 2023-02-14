package homework.annotations;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.annotation.Annotation;

import static homework.annotations.AnnotationHandler.HandlePhase.BEFORE;

@Slf4j
public class LogAnnotationHandler implements AnnotationHandler {
    @Override
    public HandlePhase getHandlePhase() {
        return BEFORE;
    }

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return Log.class;
    }

    @Override
    public void handle(AnnotationDto dto) {
        log.info("Вызван класс: {}, метод: {}, параметры: {}",
                dto.getProxy().getClass().getName(),
                dto.getMethod().getName(),
                getParametersString(dto.getArgs()));
    }

    private String getParametersString(Object[] args) {
        StringBuilder sb;
        int len;

        if (args == null)
            return "null";
        len = args.length;
        sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((args[i]));
            if (i < len - 1)
                sb.append(", ");
        }
        return sb.toString();
    }
}