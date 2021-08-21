package com.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableUtils {

    public static String tableToString(List<List<String>> tableInput,
                                       List<String> rowHeadInput,
                                       List<String> colHeadInput) {
        List<List<String>> table = tableInput.stream().map(ArrayList::new).collect(Collectors.toList());
        List<String> rowHead = new ArrayList<>(rowHeadInput == null
                ? IntStream.range(0, tableInput.size() ).mapToObj(Objects::toString).toList()
                : rowHeadInput
        );
        List<String> colHead = new ArrayList<>(colHeadInput == null
                ? IntStream.range(0, tableInput.get(0).size() ).mapToObj(Objects::toString).toList()
                : colHeadInput
        );

        table.add(0, colHead);
        table.get(0).add(0, "\\");
        IntStream.range(1, table.size()).forEach(i -> table.get(i).add(0, rowHead.get(i - 1)));

        List<String> lines = table.stream().map(o -> String.join("\t ", o)).collect(Collectors.toList());

        return String.join("\n", lines) + "\n";
    }
}
