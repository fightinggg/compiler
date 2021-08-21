package com.example.grammar;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文法
 *
 * @author wsx
 */
@Data
public class GrammarConfigImpl implements GrammarConfig {
    /**
     * 产生式集合
     */

    private Map<Production, Integer> productionIdMap;
    private Production[] productions;

    private List<Integer> terminal;
    private List<Integer> nonTerminal;
    private List<String> symbols;

    /**
     * 文法目标
     */
    private Integer target;

    private String name;
    private Integer emptyTerminal;
    private Integer endTerminal;


    public GrammarConfigImpl() {
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public Set<Integer> allTerminal() {
        return new HashSet<>(terminal);
    }

    @Override
    public Set<Integer> allNotTerminal() {
        return new HashSet<>(nonTerminal);
    }

    @Override
    public List<String> symbol() {
        return symbols;
    }

    @Override
    public Production[] allProduction() {
        return productions;
    }

    @Override
    public Integer target() {
        return target;
    }

    @Override
    public boolean isTerminal(Integer symbol) {
        return terminal.contains(symbol);
    }

    @Override
    public Set<Production> allProduction(Integer left) {
        return Arrays.stream(productions).filter(production -> production.leftSymbol().equals(left)).collect(Collectors.toSet());
    }

    @Override
    public Integer productionId(Production production) {
        return productionIdMap.get(production);
    }

    @Override
    public Map<Production, Integer> productionIdMap() {
        return productionIdMap;
    }

    @Override
    public Integer emptyTerminal() {
        return emptyTerminal;
    }

    @Override
    public Integer endTerminal() {
        return endTerminal;
    }
}
