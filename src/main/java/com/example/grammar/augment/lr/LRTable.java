package com.example.grammar.augment.lr;

import com.example.grammar.Production;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class LRTable {

    @Getter
    @AllArgsConstructor
    public static class Action {
        public static String ACC = "acc";

        private final String ac;
        private final Integer jump;

        @Override
        public String toString() {
            return ac.equals(ACC) ? ACC : ac + jump;
        }
    }

    private final List<Map<String, Integer>> gotoTable;
    private final List<Map<String, Action>> actionTable;
    private final List<Production> productions;
    private final Integer begin;

    private String tableToString(List<List<String>> tableInput) {
        List<List<String>> table = tableInput.stream().map(ArrayList::new).collect(Collectors.toList());


        int colSize = table.stream().mapToInt(List::size).max().orElse(0);
        List<String> formatStrings = IntStream.range(0, colSize).mapToObj(i ->
                table.stream()
                        .filter(row -> row.size() > i)
                        .map(row -> row.get(i))
                        .map(Object::toString)
                        .mapToInt(String::length)
                        .max().orElse(0)
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

    @Override
    public String toString() {
        List<String> terminal = actionTable.stream().map(Map::keySet).flatMap(Collection::stream).distinct().toList();
        List<String> nonTerminal = gotoTable.stream().map(Map::keySet).flatMap(Collection::stream).distinct().toList();

        int rowSize = gotoTable.size();
        int colSize = terminal.size() + nonTerminal.size();

        Stream<Map.Entry<String, Integer>> terminalStream = IntStream.range(0, terminal.size())
                .mapToObj(i -> Map.entry(terminal.get(i), i));
        Stream<Map.Entry<String, Integer>> nonTerminalStream = IntStream.range(0, nonTerminal.size())
                .mapToObj(i -> Map.entry(nonTerminal.get(i), i + terminal.size()));
        Map<String, Integer> id = Stream.concat(terminalStream, nonTerminalStream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        List<List<String>> table = new ArrayList<>();
        List<String> head = new ArrayList<>();
        head.add("");
        head.addAll(terminal);
        head.addAll(nonTerminal);
        table.add(head);

        for (int i = 0; i < rowSize; i++) {
            List<String> list = IntStream.range(0, colSize).mapToObj(o -> "").collect(Collectors.toList());

            gotoTable.get(i).forEach((k, v) -> list.set(id.get(k), v.toString()));
            actionTable.get(i).forEach((k, v) -> list.set(id.get(k), v.toString()));
            list.add(0, String.valueOf(i));
            table.add(list);
        }

        List<List<String>> productionHead = IntStream.range(0, productions.size())
                .mapToObj(i -> List.of(String.valueOf(i), productions.get(i).toString()))
                .collect(Collectors.toList());

        return tableToString(productionHead) + tableToString(table);

    }
}
