package com.example.visiable;

import com.alibaba.fastjson.JSON;
import com.example.grammar.augment.lr.slr.SLRAugmentProduction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AugmentProductionItemSetVisiable {


    public static String toDot(Map<Set<SLRAugmentProduction>, Map<String, Set<SLRAugmentProduction>>> map, Map<Set<SLRAugmentProduction>, Integer> itemId) {
        String nodes = map.keySet().stream()
                .map(o -> "\"%d\"[ label = %s]".formatted(o.hashCode(),
                        JSON.toJSONString(itemId.get(o) + "\n" + o.stream().map(Object::toString).collect(Collectors.joining("\n")))))
                .collect(Collectors.joining("\n"));

        String edges = map.entrySet().stream()
                .flatMap(kv -> kv.getValue().entrySet().stream().map(o -> List.of(kv.getKey().hashCode(),
                        o.getValue().hashCode(), o.getKey())))
                .map(o -> "\"%s\" -> \"%s\"[ label = %s]".formatted(o.get(0), o.get(1),
                        JSON.toJSONString(o.get(2))))
                .collect(Collectors.joining("\n"));


        return "digraph { \n%s\n%s\n}".formatted(nodes, edges);
    }

    public static String toTxt(Map<Set<SLRAugmentProduction>, Map<String, Set<SLRAugmentProduction>>> map, Map<Set<SLRAugmentProduction>, Integer> itemId) {
        return map.keySet().stream()
                .map(o -> itemId.get(o) + "\n" + o.stream().map(Object::toString).collect(Collectors.joining("\n")))
                .collect(Collectors.joining("\n\n"));
    }

}
