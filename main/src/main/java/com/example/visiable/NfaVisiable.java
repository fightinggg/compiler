package com.example.visiable;

import com.example.nfa.Nfa;
import com.example.utils.ToStringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NfaVisiable {


    public static String nfa2Dot(Nfa<Object, String> nfa) {
        List<List<String>> edgeList = new ArrayList<>();
        nfa.allTrans().forEach((start, edges) -> edges.forEach((edge, toList) -> toList.forEach(to -> {
            String fromString = "\"%d\"".formatted(start.hashCode());
            String toString = "\"%d\"".formatted(to.hashCode());
            edgeList.add(List.of(fromString, toString, ToStringUtils.toString(edge)));
        })));

        String edgesString = edgeList.stream()
                .collect(Collectors.groupingBy(strings -> List.of(strings.get(0), strings.get(1))))
                .entrySet().stream()
                .map(edge -> {
                    final List<String> edgeSymbol = edge.getValue().stream().map(o -> o.get(2))
                            .sorted(String::compareTo)
                            .collect(Collectors.toList());
                    final String collect = IntStream.range(0, edgeSymbol.size())
                            .boxed()
                            .collect(Collectors.groupingBy((Function<Integer, Object>) integer -> integer / 10)).values().stream().map(o -> o.stream().map(edgeSymbol::get).toList())
                            .map(Object::toString)
                            .map(o -> String.join(",", o))
                            .collect(Collectors.joining("\n"));
                    return List.of(edge.getKey().get(0), edge.getKey().get(1), collect);
                })
                .map(o -> " %s -> %s [ label = %s]".formatted(o.get(0), o.get(1), ToStringUtils.toString(o.get(2))))
                .collect(Collectors.joining("\n"));


        String start = "%s[label = \"%s\"]".formatted(nfa.startState().hashCode(), "startnode");
        String end = nfa.endStateSet().stream()
                .map(o -> "%s[label = \"%s\"]".formatted(o.hashCode(), "endnode"))
                .collect(Collectors.joining("\n"));


        return "digraph { \n%s \n%s \n%s\n }".formatted(start, end, edgesString);
    }
}
