package com.example.nfa;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 不可变Nfa
 */
@Builder
public class NfaImpl<STATE, SYMBOL> implements Nfa<STATE, SYMBOL> {
    private final Set<STATE> stateSet;
    private final Set<SYMBOL> symbolSet;
    private final Map<STATE, Map<SYMBOL, Set<STATE>>> transMap;
    private final STATE initState;
    private final Set<STATE> terminalState;

    public NfaImpl(Set<STATE> stateSet, Set<SYMBOL> symbolSet, Map<STATE, Map<SYMBOL, Set<STATE>>> transMap, STATE initState, Set<STATE> terminalState) {
        this.stateSet = stateSet.stream().collect(Collectors.toUnmodifiableSet());
        this.symbolSet = symbolSet.stream().collect(Collectors.toUnmodifiableSet());
        this.transMap = transMap; // 在下面实现了不可变
        this.initState = initState;
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
        return transMap.get(state).get(symbol).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public STATE initState() {
        return initState;
    }

    @Override
    public Set<STATE> terminalState() {
        return terminalState;
    }
}
