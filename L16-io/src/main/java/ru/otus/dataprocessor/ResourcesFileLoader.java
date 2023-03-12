package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import ru.otus.mapper.MyObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final ObjectMapper mapper;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = MyObjectMapper.getInstance();
    }

    @Override
    public List<Measurement> load() throws IOException {
        return mapper.readValue(readResource(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Measurement.class));
    }

    private byte[] readResource() throws IOException {
        try (InputStream io = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return io.readAllBytes();
        }
    }
}
