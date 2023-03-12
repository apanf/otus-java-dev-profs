package ru.otus.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementAdmixture;

public class MyObjectMapper {
    private static ObjectMapper mapper;

    private MyObjectMapper() {
    }

    public static ObjectMapper getInstance() {
        return mapper == null ? construct() : mapper;
    }

    private static ObjectMapper construct() {
        mapper = new ObjectMapper();

        mapper.addMixIn(Measurement.class, MeasurementAdmixture.class);
        return mapper;
    }
}
