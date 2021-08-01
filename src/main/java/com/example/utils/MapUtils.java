package com.example.utils;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class MapUtils {
    public static <K, V, NEWV> Map<K, NEWV> mapValues(Map<K, V> mp, BiFunction<K, V, NEWV> function) {
        return mp.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), function.apply(entry.getKey(), entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
