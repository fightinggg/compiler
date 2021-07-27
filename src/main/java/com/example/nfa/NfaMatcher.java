package com.example.nfa;


import java.util.*;
import java.util.stream.Collectors;

public class NfaMatcher<STATE, SYMBOL> {
    Nfa<STATE, SYMBOL> nfa;
    Set<STATE> currentStateSet;
    STATE currentEnd;
    SYMBOL empty;
    List<SYMBOL> eatList;


    public NfaMatcher(Nfa<STATE, SYMBOL> nfa, SYMBOL empty) {
        this.nfa = nfa;
        this.eatList = new ArrayList<>();
        this.empty = empty;
        this.currentEnd = null;
        this.currentStateSet = cluster(Set.of(nfa.startState()));
    }

    public Set<STATE> cluster(Set<STATE> set) {
        Set<STATE> cluster = new HashSet<>(set);
        Queue<STATE> queue = new ArrayDeque<>(set);
        Set<STATE> hasVis = new HashSet<>();
        while (!queue.isEmpty()) {
            STATE state = queue.poll();
            if (hasVis.contains(state)) {
                continue;
            }
            hasVis.add(state);
            Set<STATE> newState = nfa.trans(state, empty);
            cluster.addAll(newState);
            queue.addAll(newState);
        }
        return cluster;
    }

    public Map.Entry<Boolean, Object> eat(SYMBOL symbol) {
        // trans
        Set<STATE> nextStateSet = new HashSet<>(currentStateSet.stream()
                .flatMap(state -> nfa.trans(state, symbol).stream())
                .collect(Collectors.toUnmodifiableSet()));

        // cluster
        nextStateSet = cluster(nextStateSet);

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
            final Map.Entry<Boolean, Object> res = Map.entry(true, nfa.endStateInvoke(currentEnd).apply(eatList));
            clear();
            return res;
        } else {
            if (nextStateSet.isEmpty()) {
                String stk = eatList.stream().map(Objects::toString).collect(Collectors.joining());
                throw new RuntimeException("无法解析字符串 '%s%s'".formatted(stk, symbol));
            }
            currentStateSet = nextStateSet;
            currentEnd = nextEnd;
            eatList.add(symbol);
            return Map.entry(false, new Object());
        }
    }

    private void clear() {
//        this.nfa = nfa;
        this.eatList = new ArrayList<>();
//        this.empty = empty;
        this.currentEnd = null;
        this.currentStateSet = cluster(Set.of(nfa.startState()));
    }

}
