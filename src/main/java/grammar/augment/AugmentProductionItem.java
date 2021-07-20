package grammar.augment;

import grammar.Grammar;
import jdk.dynalink.beans.StaticClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class AugmentProductionItem {
    public static Set<AugmentProduction> _closure(Grammar grammar, List<AugmentProduction> productions) {
        Set<AugmentProduction> collect = grammar.allProduction()
                .stream()
                .filter(production ->
                        productions.stream()
                                .filter(o -> o.pos() != o.rightSymbol().size())
                                .anyMatch(o -> o.rightSymbol().get(o.pos()).equals(production.leftSymbol()))
                )
                .map(production -> new NormalAugmentProduction(production, 0))
                .collect(Collectors.toSet());

        collect.addAll(productions);

        return collect.size() == productions.size()
                ? collect.stream().collect(Collectors.toUnmodifiableSet())
                : _closure(grammar, collect.stream().toList());
    }

    private static Map<Set<AugmentProduction>, Set<AugmentProduction>> closureMemory = new HashMap<>();

    public static Set<AugmentProduction> closure(Grammar grammar, List<AugmentProduction> productions) {
        Set<AugmentProduction> augmentProductions = _closure(grammar, productions);
        Set<AugmentProduction> findResult = closureMemory.get(augmentProductions);
        if (findResult == null) {
            closureMemory.put(augmentProductions, augmentProductions);
            return augmentProductions;
        } else {
            return findResult;
        }
    }


    public static Set<AugmentProduction> itemGoto(Grammar grammar, Set<AugmentProduction> from, String symbol) {
        List<AugmentProduction> halfTarget = from.stream()
                .filter(o -> o.pos() != o.rightSymbol().size())
                .filter(production -> production.rightSymbol().get(production.pos()).equals(symbol))
                .map(o -> new NormalAugmentProduction(o, o.pos() + 1))
                .collect(Collectors.toList());
        return closure(grammar, halfTarget);
    }


    public static Map<Set<AugmentProduction>, Map<String, Set<AugmentProduction>>> itemSetDFA(Grammar grammar) {
        Map<Set<AugmentProduction>, Map<String, Set<AugmentProduction>>> res = new HashMap<>();

        Stack<Set<AugmentProduction>> stack = new Stack<>();

        List<AugmentProduction> begin = grammar.allProduction(grammar.target())
                .stream()
                .map(NormalAugmentProduction::new)
                .collect(Collectors.toList());

        stack.push(closure(grammar, begin));

        while (!stack.empty()) {
            Set<AugmentProduction> top = stack.pop();
            if (res.containsKey(top)) {
                continue;
            }
            Map<String, Set<AugmentProduction>> currentGotoMap = new HashMap<>();

            for (String symbol : grammar.allTerminal()) {
                Set<AugmentProduction> itemGoto = itemGoto(grammar, top, symbol);
                if (itemGoto.isEmpty()) {
                    continue;
                }
                currentGotoMap.put(symbol, itemGoto);
                stack.push(itemGoto);
            }

            for (String symbol : grammar.allNotTerminal()) {
                Set<AugmentProduction> itemGoto = itemGoto(grammar, top, symbol);
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

}
