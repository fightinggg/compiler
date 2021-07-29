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
    Set<String> allTerminal();

    /**
     * 2. 非终结符集合
     */
    Set<String> allNotTerminal();

    /**
     * 3. 产生式集合
     */
    Production[] allProduction();


    /**
     * 4. 目标
     */
    String target();

    /**
     * 辅助
     */
    boolean isTerminal(String symbol);

    Set<Production> allProduction(String left);

    Integer symbolId(String symbol);

    Map<String, Integer> symbolIdMap();

    Integer productionId(Production production);

    Map<Production, Integer> productionIdMap();

    String[] allSymbol();
}
