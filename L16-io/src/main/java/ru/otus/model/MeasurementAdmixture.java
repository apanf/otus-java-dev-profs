package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasurementAdmixture {
    public MeasurementAdmixture(@JsonProperty("name") String name, @JsonProperty("value") double value) {
    }
}
