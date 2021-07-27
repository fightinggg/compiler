package com.example.nfa;

import java.util.*;
import java.util.stream.Collectors;

public class Nfa2Dfa {
    public static Set<Object> cluster(Nfa<Object, String> nfa, Set<Object> set) {
        Set<Object> cluster = new HashSet<>(set);
        Queue<Object> queue = new ArrayDeque<>(set);
        Set<Object> hasVis = new HashSet<>();
        while (!queue.isEmpty()) {
            Object state = queue.poll();
            if (hasVis.contains(state)) {
                continue;
            }
            hasVis.add(state);
            Set<Object> newState = nfa.trans(state, Nfa.EMPTY_TRANS);
            cluster.addAll(newState);
            queue.addAll(newState);
        }
        return cluster;
    }

    public static Nfa<Object, String> toDfa(Nfa<Object, String> nfa) {
        Map<Set<Object>, Object> dfaStateMap = new HashMap<>();
        Map<Object, Map<String, Set<Object>>> trans = new HashMap<>();
        Queue<Set<Object>> dfaStateQueue = new ArrayDeque<>();
        final Set<Object> start = cluster(nfa, Set.of(nfa.startState()));
        dfaStateQueue.add(start);
        final Set<String> dfaSymbolSet = nfa.symbolSet().stream()
                .filter(s -> !s.equals(Nfa.EMPTY_TRANS))
                .collect(Collectors.toSet());
        while (!dfaStateQueue.isEmpty()) {
            Set<Object> poll = dfaStateQueue.poll();
            if (!dfaStateMap.containsKey(poll)) {
                dfaStateMap.put(poll, new Object());
                Map<String, Set<Object>> currentTrans = new HashMap<>();
                for (String symbol : dfaSymbolSet) {
                    Set<Object> nextStateSet = new HashSet<>(poll.stream()
                            .flatMap(state -> nfa.trans(state, symbol).stream())
                            .collect(Collectors.toUnmodifiableSet()));
                    if (!nextStateSet.isEmpty()) {
                        currentTrans.put(symbol, cluster(nfa, nextStateSet));
                    }
                }
                dfaStateQueue.addAll(currentTrans.values());
                trans.put(dfaStateMap.get(poll), currentTrans);
            }
        }
        trans.forEach((k, v) -> {
            v.forEach((edge, to) -> v.put(edge, Set.of(dfaStateMap.get(to))));
        });
        return new NfaImpl<>(new HashSet<>(dfaStateMap.values()),
                dfaSymbolSet,
                trans,
                dfaStateMap.get(start),
                new HashSet<>());
    }
}
