package com.example.grammar.augment.lr.lr1;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFirstSet;
import com.example.grammar.augment.lr.slr.SLRAugmentProduction;
import com.example.grammar.augment.lr.slr.SLRAugmentProductionImpl;

import java.util.*;
import java.util.stream.Collectors;

public class LR1AugmentProductionItem {
    private static Set<LR1AugmentProduction> _closure(GrammarConfig grammarConfig, List<LR1AugmentProduction> productions, Map<Integer, Set<Integer>> firstSet) {
        Set<LR1AugmentProduction> collect = Arrays.stream(grammarConfig.allProduction())
                .filter(production ->
                        productions.stream()
                                .filter(o -> o.pos() != o.rightSymbol().size())
                                .anyMatch(o -> o.rightSymbol().get(o.pos()).equals(production.leftSymbol()))
                )
                .map(production -> {
                    List<Integer> next = productions.stream()
                            .filter(o -> o.pos() != o.rightSymbol().size())
                            .filter(o -> o.rightSymbol().get(o.pos()).equals(production.leftSymbol()))
                            .map(o -> o.pos() + 1 == o.rightSymbol().size() ? o.next() : firstSet.get(o.rightSymbol().get(o.pos() + 1)))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    return new LR1AugmentProductionImpl(production, 0, next);
                })
                .collect(Collectors.toSet());

        collect.addAll(productions);

        collect = collect.stream().collect(Collectors.groupingBy(o -> new SLRAugmentProductionImpl(o, o.pos())))
                .entrySet().stream()
                .map(kv -> {
                    SLRAugmentProduction production = kv.getKey();
                    List<Integer> next = kv.getValue().stream().flatMap(o -> o.next().stream()).distinct().collect(Collectors.toList());
                    return new LR1AugmentProductionImpl(production, production.pos(), next);
                })
                .collect(Collectors.toSet());

        return collect.equals(new HashSet<>(productions))
                ? collect.stream().collect(Collectors.toUnmodifiableSet())
                : _closure(grammarConfig, collect.stream().toList(), firstSet);
    }

    private static final Map<Set<LR1AugmentProduction>, Set<LR1AugmentProduction>> closureMemory = new HashMap<>();

    private static Set<LR1AugmentProduction> closure(GrammarConfig grammarConfig, List<LR1AugmentProduction> productions, Map<Integer, Set<Integer>> firstSet) {
        Set<LR1AugmentProduction> LR1AugmentProductions = _closure(grammarConfig, productions, firstSet);
        Set<LR1AugmentProduction> findResult = closureMemory.get(LR1AugmentProductions);
        if (findResult == null) {
            closureMemory.put(LR1AugmentProductions, LR1AugmentProductions);
            return LR1AugmentProductions;
        } else {
            return findResult;
        }
    }


    private static Set<LR1AugmentProduction> itemGoto(GrammarConfig grammarConfig, Set<LR1AugmentProduction> from, Integer symbol, Map<Integer, Set<Integer>> firstSet) {
        List<LR1AugmentProduction> halfTarget = from.stream()
                .filter(o -> o.pos() != o.rightSymbol().size())
                .filter(production -> production.rightSymbol().get(production.pos()).equals(symbol))
                .map(o -> new LR1AugmentProductionImpl(o, o.pos() + 1, o.next()))
                .collect(Collectors.toList());
        return closure(grammarConfig, halfTarget, firstSet);
    }


    public static Map<Set<LR1AugmentProduction>, Map<Integer, Set<LR1AugmentProduction>>> itemSetDfa(GrammarConfig grammarConfig) {
        Map<Set<LR1AugmentProduction>, Map<Integer, Set<LR1AugmentProduction>>> res = new HashMap<>();
        Map<Integer, Set<Integer>> firstSet = GrammarFirstSet.firstSet(grammarConfig);

        Stack<Set<LR1AugmentProduction>> stack = new Stack<>();

        stack.push(begin(grammarConfig, firstSet));

        while (!stack.empty()) {
            Set<LR1AugmentProduction> top = stack.pop();
            if (res.containsKey(top)) {
                continue;
            }
            Map<Integer, Set<LR1AugmentProduction>> currentGotoMap = new HashMap<>();

            for (Integer symbol : grammarConfig.allTerminal()) {
                Set<LR1AugmentProduction> itemGoto = itemGoto(grammarConfig, top, symbol, firstSet);
                if (itemGoto.isEmpty()) {
                    continue;
                }
                currentGotoMap.put(symbol, itemGoto);
                stack.push(itemGoto);
            }

            for (Integer symbol : grammarConfig.allNotTerminal()) {
                Set<LR1AugmentProduction> itemGoto = itemGoto(grammarConfig, top, symbol, firstSet);
                if (itemGoto.isEmpty()) {
                    continue;
                }
                currentGotoMap.put(symbol, itemGoto);
                stack.push(itemGoto);
            }
            res.put(top, currentGotoMap);
        }

        return res;
    }

    public static Set<LR1AugmentProduction> begin(GrammarConfig grammarConfig) {
        List<LR1AugmentProduction> begin = grammarConfig.allProduction(grammarConfig.target())
                .stream()
                .map(production -> new LR1AugmentProductionImpl(production, 0, List.of(grammarConfig.endTerminal())))
                .collect(Collectors.toList());

        return closure(grammarConfig, begin, GrammarFirstSet.firstSet(grammarConfig));
    }

    public static Set<LR1AugmentProduction> begin(GrammarConfig grammarConfig, Map<Integer, Set<Integer>> firstSet) {
        List<LR1AugmentProduction> begin = grammarConfig.allProduction(grammarConfig.target())
                .stream()
                .map(production -> new LR1AugmentProductionImpl(production, 0, List.of(grammarConfig.endTerminal())))
                .collect(Collectors.toList());

        return closure(grammarConfig, begin, firstSet);
    }
}
