package com.example.nfa;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 1. 一个有穷的状态集合S<br/>
 * 2. 一个输入符号集合∑<br/>
 * 3. 一个转换函数<br/>
 * 4. 一个初始状态<br/>
 * 5. 一个终结状态集合<br/>
 */
public interface Nfa<STATE, SYMBOL> {
    String EMPTY_TRANS = "$empty";

    Set<STATE> stateSet();

    Set<SYMBOL> symbolSet();

    Set<STATE> trans(STATE state, SYMBOL symbol);

    Map<STATE, Map<SYMBOL, Set<STATE>>> allTrans();

    STATE startState();

    Set<STATE> endStateSet();

    Function<List<SYMBOL>, Object> endStateInvoke(STATE state);

    Map<STATE, Function<List<SYMBOL>, Object>> endStateInvokeMap();

    Nfa<STATE, SYMBOL> deepCopy();
}
