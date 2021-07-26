package com.example.nfa;


import java.util.*;
import java.util.stream.Collectors;

public class NfaMatcher<STATE, SYMBOL> {
    Nfa<STATE, SYMBOL> nfa;
    Set<STATE> currentStateSet;
    STATE currentEnd;
    SYMBOL empty;
    List<SYMBOL> eatList;

    NfaMatcher(Nfa<STATE, SYMBOL> nfa, SYMBOL empty) {
        this.nfa = nfa;
        eatList = new ArrayList<>();
        currentStateSet = Set.of(nfa.startState());
        this.empty = empty;
        currentEnd = null;
    }

    Map.Entry<Boolean, Object> eat(SYMBOL symbol) {
        // trans
        final Set<STATE> nextStateSet = new HashSet<>(currentStateSet.stream()
                .flatMap(state -> nfa.trans(state, symbol).stream())
                .collect(Collectors.toUnmodifiableSet()));

        // cluster
        Queue<STATE> queue = new ArrayDeque<>(nextStateSet);
        Set<STATE> hasVis = new HashSet<>();
        while (!queue.isEmpty()) {
            STATE state = queue.poll();
            if (hasVis.contains(state)) {
                continue;
            }
            hasVis.add(state);
            Set<STATE> newState = nfa.trans(state, empty);
            nextStateSet.addAll(newState);
            queue.addAll(newState);
        }


        final List<STATE> ends = nextStateSet.stream().filter(o -> nfa.endStateSet().contains(o)).collect(Collectors.toList());
        STATE nextEnd;

        if (ends.size() > 1) {
            throw new RuntimeException("正则冲突");
        } else if (ends.size() == 1) {
            nextEnd = ends.get(0);
        } else {
            nextEnd = null;
        }

        if (nextEnd != currentEnd && currentEnd != null) {
            return Map.entry(true, nfa.endStateInvoke(currentEnd).apply(eatList));
        } else {
            currentStateSet = nextStateSet;
            currentEnd = nextEnd;
            eatList.add(symbol);
            return Map.entry(false, null);
        }
    }
}
