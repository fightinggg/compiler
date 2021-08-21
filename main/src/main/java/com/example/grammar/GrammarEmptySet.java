package com.example.grammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GrammarEmptySet {
    public static Set<Integer> emptySet(GrammarConfig grammarConfig) {
        Set<Integer> emptySet = new HashSet<>();
        while (true) {
            Set<Integer> finalEmptySet = emptySet;
            Set<Integer> newEmptySet = Arrays.stream(grammarConfig.allProduction())
                    .filter(o -> finalEmptySet.containsAll(o.rightSymbol()))
                    .map(Production::leftSymbol)
                    .collect(Collectors.toSet());
            if (emptySet.size() == newEmptySet.size()) {
                break;
            } else {
                emptySet = newEmptySet;
            }
        }
        return emptySet;
    }
}
