package com.example.visiable;

import com.example.nfa.Nfa;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NfaVisiable {


    public static String nfa2Dot(Nfa<Object, String> nfa) {
        List<String> edgeList = new ArrayList<>();
        nfa.allTrans().forEach((start, edges) -> edges.forEach((edge, toList) -> toList.forEach(to -> {
            String fromString = "\"%d\"".formatted(start.hashCode());
            String toString = "\"%d\"".formatted(to.hashCode());
            edgeList.add(" %s -> %s [ label = \"%s\"]".formatted(fromString, toString, edge));
        })));

        String edgesString = String.join("\n", edgeList);

        String start = "%s[label = \"%s\"]".formatted(nfa.startStateSet().hashCode(), "startnode");
        String end = nfa.endStateSet().stream()
                .map(o -> "%s[label = \"%s\"]".formatted(o.hashCode(), "endnode"))
                .collect(Collectors.joining("\n"));


        return "digraph { \n%s \n%s \n%s\n }".formatted(start, end, edgesString);
    }
}
