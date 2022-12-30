package ru.otus.panferov;

import java.util.List;

import com.google.common.base.Joiner;

public class Main {
    public static void main(String[] args) {
        List<String> strings = List.of("one", "two", "три", "four");
        String separator = "; ";

        System.out.println(joinArguments(separator, strings));

    }

    public static String joinArguments(String separator, List<String> strings) {
        return Joiner.on(separator).skipNulls().join(strings);
    }
}
