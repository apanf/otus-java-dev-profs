package homework.annotations;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static homework.annotations.AnnotationHandler.HandlePhase.BEFORE;

@Slf4j
public class SaveAnnotationHandler implements AnnotationHandler {
    @Override
    public HandlePhase getHandlePhase() {
        return BEFORE;
    }

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return Save.class;
    }

    @Override
    public void handle(AnnotationDto dto) {
        Path file;
        try {
            file = Files.createTempFile(null, null);

            Files.writeString(file, dto.toString(), Charset.forName("windows-1251"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Параметры метода сохранены в файл {}", file.toAbsolutePath());
    }
}
