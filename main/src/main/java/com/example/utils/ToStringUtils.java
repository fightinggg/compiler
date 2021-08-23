package com.example.utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ToStringUtils {
    public static String toString(Object obj) {
        String s = obj.toString();
        return "\"" + IntStream.range(0, s.length()).boxed()
                .map(s::charAt)
                .map(o -> o == '\\' ? "\\\\" : o == '"' ? "\\\"" : o.toString())
                .collect(Collectors.joining()) + "\"";
    }
}
