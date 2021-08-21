package com.example.grammar.augment.lr.slr;

import com.example.grammar.GrammarConfig;

import java.util.*;
import java.util.stream.Collectors;

public class SLRAugmentProductionItem {
    private static Set<SLRAugmentProduction> _closure(GrammarConfig grammarConfig, List<SLRAugmentProduction> productions) {
        Set<SLRAugmentProduction> collect = Arrays.stream(grammarConfig.allProduction())
                .filter(production ->
                        productions.stream()
                                .filter(o -> o.pos() != o.rightSymbol().size())
                                .anyMatch(o -> o.rightSymbol().get(o.pos()).equals(production.leftSymbol()))
                )
                .map(production -> new SLRAugmentProductionImpl(production, 0))
                .collect(Collectors.toSet());

        collect.addAll(productions);

        return collect.size() == productions.size()
                ? collect.stream().collect(Collectors.toUnmodifiableSet())
                : _closure(grammarConfig, collect.stream().toList());
    }

    private static Map<Set<SLRAugmentProduction>, Set<SLRAugmentProduction>> closureMemory = new HashMap<>();

    private static Set<SLRAugmentProduction> closure(GrammarConfig grammarConfig, List<SLRAugmentProduction> productions) {
        Set<SLRAugmentProduction> SLRAugmentProductions = _closure(grammarConfig, productions);
        Set<SLRAugmentProduction> findResult = closureMemory.get(SLRAugmentProductions);
        if (findResult == null) {
            closureMemory.put(SLRAugmentProductions, SLRAugmentProductions);
            return SLRAugmentProductions;
        } else {
            return findResult;
        }
    }


    private static Set<SLRAugmentProduction> itemGoto(GrammarConfig grammarConfig, Set<SLRAugmentProduction> from, Integer symbol) {
        List<SLRAugmentProduction> halfTarget = from.stream()
                .filter(o -> o.pos() != o.rightSymbol().size())
                .filter(production -> production.rightSymbol().get(production.pos()).equals(symbol))
                .map(o -> new SLRAugmentProductionImpl(o, o.pos() + 1))
                .collect(Collectors.toList());
        return closure(grammarConfig, halfTarget);
    }


    public static Map<Set<SLRAugmentProduction>, Map<Integer, Set<SLRAugmentProduction>>> itemSetDfa(GrammarConfig grammarConfig) {
        Map<Set<SLRAugmentProduction>, Map<Integer, Set<SLRAugmentProduction>>> res = new HashMap<>();

        Stack<Set<SLRAugmentProduction>> stack = new Stack<>();

        stack.push(begin(grammarConfig));

        while (!stack.empty()) {
            Set<SLRAugmentProduction> top = stack.pop();
            if (res.containsKey(top)) {
                continue;
            }
            Map<Integer, Set<SLRAugmentProduction>> currentGotoMap = new HashMap<>();

            for (Integer symbol : grammarConfig.allTerminal()) {
                Set<SLRAugmentProduction> itemGoto = itemGoto(grammarConfig, top, symbol);
                if (itemGoto.isEmpty()) {
                    continue;
                }
                currentGotoMap.put(symbol, itemGoto);
                stack.push(itemGoto);
            }

            for (Integer symbol : grammarConfig.allNotTerminal()) {
                Set<SLRAugmentProduction> itemGoto = itemGoto(grammarConfig, top, symbol);
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

    public static Set<SLRAugmentProduction> begin(GrammarConfig grammarConfig) {
        List<SLRAugmentProduction> begin = grammarConfig.allProduction(grammarConfig.target())
                .stream()
                .map(SLRAugmentProductionImpl::new)
                .collect(Collectors.toList());

        return closure(grammarConfig, begin);
    }
}
