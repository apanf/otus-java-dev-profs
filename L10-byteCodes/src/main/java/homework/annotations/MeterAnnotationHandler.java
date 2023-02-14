package homework.annotations;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Map;

import static homework.annotations.AnnotationHandler.HandlePhase.AROUND;

@Slf4j
public class MeterAnnotationHandler implements AnnotationHandler {
    @Override
    public HandlePhase getHandlePhase() {
        return AROUND;
    }

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return Meter.class;
    }

    @Override
    public void handle(AnnotationDto dto) {
        var payload = dto.getPayload();

        if (!payload.containsKey("meter"))
            firstRun(payload);
        else
            secondRun(payload);
    }

    private void firstRun(Map<String, Object> payload) {
        log.info("Замер времени выполнения метода");
        payload.put("meter", System.nanoTime());
    }

    private void secondRun(Map<String, Object> payload) {
        long result = System.nanoTime() - (long) payload.get("meter");
        log.info("Время выполнения метода, нс: {}", result);
    }
}
