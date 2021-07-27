package com.example.nfa;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NfaUtils {
    /**
     * start --symbol--> [end]
     */
    public static <SYMBOL> Nfa<Object, SYMBOL> oneChar(SYMBOL symbol) {
        Object startState = new Object();
        Object endState = new Object();
        Set<Object> stateSet = Set.of(startState, endState);
        Set<SYMBOL> symbolSet = Set.of(symbol);
        Map<Object, Map<SYMBOL, Set<Object>>> trans = Map.of(startState, Map.of(symbol, Set.of(endState)));
        Set<Object> endStateSet = Set.of(endState);
        return new NfaImpl<>(stateSet, symbolSet, trans, startState, endStateSet);
    }

    public static <SYMBOL> Nfa<Object, SYMBOL> someCharParallel(List<SYMBOL> symbols) {
        Object startState = new Object();
        Object endState = new Object();
        Set<Object> stateSet = Set.of(startState, endState);
        Set<SYMBOL> symbolSet = Set.copyOf(new HashSet<>(symbols));

        Map<Object, Map<SYMBOL, Set<Object>>> trans = Map.of(startState,
                Map.copyOf(symbols.stream().collect(Collectors.toMap(o -> o, o -> Set.of(endState)))));
        Set<Object> endStateSet = Set.of(endState);
        return new NfaImpl<>(stateSet, symbolSet, trans, startState, endStateSet);
    }

    /**
     * start --symbol--> [end]
     */
    public static <SYMBOL> Nfa<Object, SYMBOL> selfLoop(Nfa<Object, SYMBOL> nfa, SYMBOL symbol) {
        Set<Object> stateSet = nfa.stateSet();
        Set<SYMBOL> symbolSet = new HashSet<>(nfa.symbolSet());
        symbolSet.add(symbol);
        Object startState = nfa.startState();
        Set<Object> endStateSet = nfa.endStateSet();
        Map<Object, Map<SYMBOL, Set<Object>>> trans = new HashMap<>(nfa.allTrans());
        endStateSet.forEach(
                endState -> {

                    Set<Object> nwTrans = Stream.concat(nfa.trans(endState, symbol).stream(), Stream.of(startState)).collect(Collectors.toSet());
                    Map<SYMBOL, Set<Object>> nwTransMap = new HashMap<>(trans.getOrDefault(endState, new HashMap<>()));
                    nwTransMap.put(symbol, nwTrans);
                    trans.put(endState, nwTransMap);
                }
        );
        final NfaImpl<Object, SYMBOL> res = new NfaImpl<>(stateSet, symbolSet, trans, startState, endStateSet);
        res.setEndStateInvoke(res.endStateInvokeMap());
        return res;
    }


    public static <SYMBOL> Nfa<Object, SYMBOL> series(Nfa<Object, SYMBOL> nfa1, Nfa<Object, SYMBOL> nfa2, SYMBOL symbol) {
        if (nfa1 == nfa2) {
            throw new RuntimeException("please not using series(nfa,nfa,?)");
        }
        Set<Object> stateSet = new HashSet<>(nfa1.stateSet());
        stateSet.addAll(nfa2.stateSet());

        Set<SYMBOL> symbolSet = new HashSet<>(nfa1.symbolSet());
        symbolSet.addAll(nfa2.symbolSet());
        symbolSet.add(symbol);

        Object startState = nfa1.startState();
        Set<Object> endStateSet = nfa2.endStateSet();
        Map<Object, Map<SYMBOL, Set<Object>>> trans = new HashMap<>(nfa1.allTrans());
        trans.putAll(nfa2.allTrans());

        nfa1.endStateSet().forEach(
                endState -> {
                    Set<Object> terminalTrans = trans.getOrDefault(endState, new HashMap<>()).getOrDefault(symbol, new HashSet<>());
                    Set<Object> nwTrans = Stream.concat(terminalTrans.stream(), Stream.of(nfa2.startState())).collect(Collectors.toSet());
                    Map<SYMBOL, Set<Object>> nwTransMap = new HashMap<>(trans.getOrDefault(endState, new HashMap<>()));
                    nwTransMap.put(symbol, nwTrans);
                    trans.put(endState, nwTransMap);
                }
        );

        final NfaImpl<Object, SYMBOL> res = new NfaImpl<>(stateSet, symbolSet, trans, startState, endStateSet);
        res.setEndStateInvoke(nfa2.endStateInvokeMap());
        return res;
    }

    public static <SYMBOL> Nfa<Object, SYMBOL> parallel(Nfa<Object, SYMBOL> nfa1, Nfa<Object, SYMBOL> nfa2, SYMBOL symbol) {
        if (nfa1 == nfa2) {
            throw new RuntimeException("please not using series(nfa,nfa,?)");
        }
        Object startState = new Object();

        Set<Object> stateSet = new HashSet<>(nfa1.stateSet());
        stateSet.addAll(nfa2.stateSet());
        stateSet.add(startState);


        Set<SYMBOL> symbolSet = new HashSet<>(nfa1.symbolSet());
        symbolSet.addAll(nfa2.symbolSet());
        symbolSet.add(symbol);

        Set<Object> endStateSet = Stream.concat(nfa1.endStateSet().stream(), nfa2.endStateSet().stream()).collect(Collectors.toSet());
        Map<Object, Map<SYMBOL, Set<Object>>> trans = new HashMap<>(nfa1.allTrans());
        trans.putAll(nfa2.allTrans());
        trans.put(startState, Map.of(symbol, Set.of(nfa1.startState(), nfa2.startState())));

        final NfaImpl<Object, SYMBOL> res = new NfaImpl<>(stateSet, symbolSet, trans, startState, endStateSet);
        final Map<Object, Function<List<SYMBOL>, Object>> collect = Stream.concat(nfa1.endStateInvokeMap().entrySet().stream(), nfa2.endStateInvokeMap().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        res.setEndStateInvoke(collect);
        return res;
    }

    public static <SYMBOL> Nfa<Object, SYMBOL> series(List<Nfa<Object, SYMBOL>> nfas, SYMBOL symbol) {
        Nfa<Object, SYMBOL> res = nfas.get(0);
        for (int i = 1; i < nfas.size(); i++) {
            res = series(res, nfas.get(i), symbol);
        }
        return res;
    }

    public static <SYMBOL> Nfa<Object, SYMBOL> parallel(List<Nfa<Object, SYMBOL>> nfas, SYMBOL symbol) {
        Nfa<Object, SYMBOL> res = nfas.get(0);
        for (int i = 1; i < nfas.size(); i++) {
            res = parallel(res, nfas.get(i), symbol);
        }
        return res;
    }


}
