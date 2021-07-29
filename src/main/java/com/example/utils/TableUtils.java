package com.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableUtils {
    public static String tableToString(List<List<String>> tableInput) {
        List<List<String>> table = tableInput.stream().map(ArrayList::new).collect(Collectors.toList());


        int colSize = table.stream().mapToInt(List::size).max().orElse(1);
        List<String> formatStrings = IntStream.range(0, colSize).mapToObj(i ->
                table.stream()
                        .filter(row -> row.size() > i)
                        .map(row -> row.get(i))
                        .map(Object::toString)
                        .mapToInt(String::length)
                        .map(value -> value + 1)
                        .max().orElse(1)
        ).map(o -> "%-" + o + "s").toList();

        IntStream.range(0, colSize).forEach(i ->
                table.stream()
                        .filter(row -> row.size() > i)
                        .forEach(row -> row.set(i, formatStrings.get(i).formatted(row.get(i))))
        );

        table.forEach(row -> row.add(""));
        table.forEach(row -> row.add(0, ""));
        List<String> lines = table.stream().map(o -> String.join("|", o)).collect(Collectors.toList());
        lines.add(0, "-".repeat(lines.get(0).length()));
        lines.add("-".repeat(lines.get(0).length()));


        return String.join("\n", lines) + "\n";
    }
}
