package com.example.grammar.augment.lr;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class LRTable {

    @Getter
    @AllArgsConstructor
    public static class Action {
        private final String ac;
        private final String jump;
    }

    private final Map<String, Integer>[] gotoTable;
    private final Map<String, String> actionTable;

}
