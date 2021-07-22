package com.example.grammar.augment.lr;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class LRTable {

    @Getter
    @AllArgsConstructor
    public static class Action {
        private final String ac;
        private final String jump;

        @Override
        public String toString() {
            return ac + jump;
        }
    }

    private final List<Map<String, Integer>> gotoTable;
    private final List<Map<String, Action>> actionTable;

    @Override
    public String toString() {
        List<String> terminal = actionTable.stream().map(Map::keySet).flatMap(Collection::stream).distinct().toList();
        List<String> nonTerminal = gotoTable.stream().map(Map::keySet).flatMap(Collection::stream).distinct().toList();

        int rowSize = gotoTable.size();
        int colSize = terminal.size() + nonTerminal.size();

        Map<String, Integer> id = new HashMap<>();
        final int[] idBegin = {0};
        terminal.forEach(o -> id.put(o, idBegin[0]++));
        nonTerminal.forEach(o -> id.put(o, idBegin[0]++));


        List<List<String>> table = new ArrayList<>();
        List<String> head = new ArrayList<>();
        head.addAll(terminal);
        head.addAll(nonTerminal);
        table.add(head);

        for (int i = 0; i < rowSize; i++) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < colSize; j++) {
                list.add("");
            }
            gotoTable.get(i).forEach((k, v) -> list.set(id.get(k), v.toString()));
            actionTable.get(i).forEach((k, v) -> list.set(id.get(k), v.toString()));
            table.add(list);
        }

        List<Integer> formatLengths = new ArrayList<>();
        for (int j = 0; j <= colSize; j++) {
            formatLengths.add(1);
        }

        for (int i = 0; i < table.size(); i++) {
            List<String> row = table.get(i);
            row.add(0, i == 0 ? "" : "%d".formatted(i - 1));
            for (int j = 0; j < row.size(); j++) {
                formatLengths.set(j, Math.max(formatLengths.get(j), row.get(j).length()));
            }
        }
        List<String> formatStrings = formatLengths.stream().map(o -> "%" + o + "s").toList();

        for (int i = 0; i < table.size(); i++) {
            List<String> row = table.get(i);
            for (int j = 0; j < row.size(); j++) {
                row.set(j, formatStrings.get(j).formatted(row.get(j)));
            }
            row.add(0, "");
            row.add("");
            table.set(i, row);
        }

        List<String> lines = table.stream().map(o -> String.join("|", o)).collect(Collectors.toList());
        lines.add(0, "-".repeat(lines.get(0).length()));
        lines.add("-".repeat(lines.get(0).length()));

        return String.join("\n", lines);
    }
}
