package homework.annotations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public interface AnnotationHandler {
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class AnnotationDto {
        private Object proxy;
        private Method method;
        private Object[] args;
        private Map<String, Object> payload;

        public Map<String, Object> getPayload() {
            return payload == null ? payload = new HashMap<>() : payload;
        }
    }

    enum HandlePhase {
        BEFORE, AFTER, AROUND;
    }

    HandlePhase getHandlePhase();

    Class<? extends Annotation> getAnnotationType();

    void handle(AnnotationDto dto);
}