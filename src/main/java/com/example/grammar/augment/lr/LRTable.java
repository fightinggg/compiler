package com.example.grammar.augment.lr;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class LRTable {

    @Getter
    @AllArgsConstructor
    public static class Action {
        private final String ac;
        private final String jump;
    }

    private final List<Map<String, Integer>> gotoTable;
    private final List<Map<String, Action>> actionTable;

    @Override
    public String toString() {
        int rowSize = gotoTable.size();
        int terminalSize = (int) actionTable.stream().map(Map::keySet).flatMap(Collection::stream).distinct().count();
        int nonTerminalSize = (int) gotoTable.stream().map(Map::keySet).flatMap(Collection::stream).distinct().count();
        int colSize = terminalSize + nonTerminalSize;

        List<List<String>> table = new ArrayList<>();
        for (int i = 0; i < rowSize; i++) {
            table.add(new ArrayList<>());
            for (int j = 0; j < colSize; j++) {
                table.get(i).add("");
            }
        }

        return table.stream().map(o -> String.join("|", o)).collect(Collectors.joining("\n"));
    }
}
