package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.mapper.MyObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        mapper = MyObjectMapper.getInstance();
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        try (BufferedWriter os = new BufferedWriter(new FileWriter(fileName))) {
            mapper.writeValue(os, data);
        }
    }
}
