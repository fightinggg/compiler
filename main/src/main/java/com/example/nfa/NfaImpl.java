package com.example.nfa;

import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 不可变Nfa
 */
public class NfaImpl<STATE, SYMBOL> implements Nfa<STATE, SYMBOL> {
    private final Set<STATE> stateSet;
    private final Set<SYMBOL> symbolSet;
    private final Map<STATE, Map<SYMBOL, Set<STATE>>> transMap;
    private final STATE startStateSet;
    private final Set<STATE> terminalState;

    @Setter
    private Map<STATE, Function<List<SYMBOL>, Object>> endStateInvoke = new HashMap<>();
    @Setter
    private Map<STATE, Integer> endStateInvokeOrder = new HashMap<>();

    private Map<SYMBOL, Set<STATE>> deepCopy(Map<SYMBOL, Set<STATE>> mp) {
        Map<SYMBOL, Set<STATE>> copy = mp.entrySet().stream()
                .map(o -> Map.entry(o.getKey(), Set.copyOf(o.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Map.copyOf(copy);
    }

    public NfaImpl(Set<STATE> stateSet, Set<SYMBOL> symbolSet, Map<STATE, Map<SYMBOL, Set<STATE>>> transMap, STATE startStateSet, Set<STATE> terminalState) {
        this.stateSet = stateSet.stream().collect(Collectors.toUnmodifiableSet());
        this.symbolSet = symbolSet.stream().collect(Collectors.toUnmodifiableSet());
        Map<STATE, Map<SYMBOL, Set<STATE>>> copy = transMap.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), deepCopy(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.transMap = Map.copyOf(copy);
        this.startStateSet = startStateSet;
        this.terminalState = terminalState.stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<STATE> stateSet() {
        return stateSet;
    }

    @Override
    public Set<SYMBOL> symbolSet() {
        return symbolSet;
    }

    @Override
    public Set<STATE> trans(STATE state, SYMBOL symbol) {
        return transMap.getOrDefault(state, new HashMap<>()).getOrDefault(symbol, new HashSet<>());
    }

    @Override
    public Map<STATE, Map<SYMBOL, Set<STATE>>> allTrans() {
        return transMap;
    }

    @Override
    public STATE startState() {
        return startStateSet;
    }

    @Override
    public Set<STATE> endStateSet() {
        return terminalState;
    }

    @Override
    public Function<List<SYMBOL>, Object> endStateInvoke(STATE state) {
        return endStateInvoke.getOrDefault(state, symbols -> null);
    }

    @Override
    public Map<STATE, Function<List<SYMBOL>, Object>> endStateInvokeMap() {
        return endStateInvoke;
    }

    @Override
    public Integer endStateInvokeOrder(STATE state) {
        return endStateInvokeOrder.get(state);
    }

    @Override
    public Map<STATE, Integer> endStateOrderMap() {
        return endStateInvokeOrder;
    }

    @Override
    public Nfa<STATE, SYMBOL> deepCopy() {
        // TODO
        return null;
    }
}
