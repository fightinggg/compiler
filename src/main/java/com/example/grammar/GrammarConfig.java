package com.example.grammar;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文法四要素
 * 1. 终结符集合
 * 2. 非终结符集合
 * 3. 产生式集合
 * 4. 目标
 */
public interface GrammarConfig {
    String name();

    /**
     * 1. 终结符集合
     */
    Set<Integer> allTerminal();

    /**
     * 2. 非终结符集合
     */
    Set<Integer> allNotTerminal();

    List<String> symbol();

    /**
     * 3. 产生式集合
     */
    Production[] allProduction();


    /**
     * 4. 目标
     */
    Integer target();

    /**
     * 辅助
     */
    boolean isTerminal(Integer symbol);

    Set<Production> allProduction(Integer left);

    Integer productionId(Production production);

    Map<Production, Integer> productionIdMap();

    Integer emptyTerminal();

    Integer endTerminal();


}
