package com.example.visiable;

import com.example.grammar.GrammarConfig;
import com.example.grammar.Production;
import com.example.utils.ToStringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductionItemSetVisiable {


    public static <T extends Production> String toDot(Map<Set<T>, Map<Integer, Set<T>>> map, Map<Set<T>, Integer> itemId, GrammarConfig grammarConfig) {
        String nodes = map.keySet().stream()
                .map(o -> "\"%d\"[ label = %s]".formatted(o.hashCode(),
                        ToStringUtils.toString(itemId.get(o) + "\n" + o.stream().map(Object::toString).collect(Collectors.joining("\n")))))
                .collect(Collectors.joining("\n"));

        String edges = map.entrySet().stream()
                .flatMap(kv -> kv.getValue().entrySet().stream().map(o ->
                        List.of(kv.getKey().hashCode(), o.getValue().hashCode(), grammarConfig.symbol().get(o.getKey()))))
                .map(o -> "\"%s\" -> \"%s\"[ label = %s]".formatted(o.get(0), o.get(1),
                        ToStringUtils.toString(o.get(2))))
                .collect(Collectors.joining("\n"));


        return "digraph { \n%s\n%s\n}".formatted(nodes, edges);
    }

    public static <T extends Production> String toTxt(Map<Set<T>, Map<Integer, Set<T>>> map, Map<Set<T>, Integer> itemId) {
        return map.keySet().stream()
                .map(o -> itemId.get(o) + "\n" + o.stream().map(Object::toString).collect(Collectors.joining("\n")))
                .collect(Collectors.joining("\n\n"));
    }

}
