package com.example.grammar;

import java.util.*;
import java.util.stream.Collectors;

public class GrammarFollowSet {

    /**
     * A -> BCDEFG...XYZ
     * <p>
     * FOLLOW B += FIRST C
     * if C can empty , FOLLOW B += FIRST D
     * if D can empty , FOLLOW B += FIRST E
     * ...
     * <p>
     * FOLLOW Z += FOLLOW A
     * if Z can empty, FOLLOW Y += FOLLOW A
     *
     * @param grammarConfig
     * @return
     */
    public static Map<Integer, Set<Integer>> followSet(GrammarConfig grammarConfig) {

        Map<Integer, Set<Integer>> firstSet = GrammarFirstSet.firstSet(grammarConfig);

        Map<Integer, Set<Integer>> followSet = new HashMap<>();

        followSet.put(grammarConfig.target(), new HashSet<>(Set.of(grammarConfig.endTerminal())));

        Arrays.stream(grammarConfig.allProduction())
                .forEach(production -> {
                    List<Integer> list = production.rightSymbol();
                    for (int i = 0; i + 1 < list.size(); i++) {
                        followSet.putIfAbsent(list.get(i), new HashSet<>());
                        followSet.get(list.get(i)).addAll(firstSet.get(list.get(i + 1)));
                        for (int j = i + 1; j + 1 < list.size() && firstSet.get(list.get(j)).contains(grammarConfig.endTerminal()); j++) {
                            followSet.get(list.get(i)).addAll(firstSet.get(list.get(j + 1)));
                        }
                    }
                });


        Set<Integer> emptySet = GrammarEmptySet.emptySet(grammarConfig);
        Set<Map.Entry<Integer, Integer>> allDepends = Arrays.stream(grammarConfig.allProduction())
                .filter(o -> !o.rightSymbol().isEmpty())
                .flatMap(production -> {
                    List<Integer> list = production.rightSymbol();
                    List<Map.Entry<Integer, Integer>> flat = new ArrayList<>();
                    flat.add(Map.entry(list.get(list.size() - 1), production.leftSymbol()));
                    for (int i = list.size() - 1; i >= 1 && emptySet.contains(list.get(i)); i--) {
                        flat.add(Map.entry(list.get(i - 1), production.leftSymbol()));
                    }
                    return flat.stream();
                })
                .collect(Collectors.toSet());

        // floyd algorithm
        final boolean[] upd = {true};
        while (upd[0]) {
            upd[0] = false;
            allDepends.forEach(o -> {
                followSet.putIfAbsent(o.getKey(), new HashSet<>());
                followSet.putIfAbsent(o.getValue(), new HashSet<>());

                upd[0] = upd[0] || followSet.get(o.getKey()).addAll(followSet.get(o.getValue()));
            });
        }

        return followSet;
    }
}
