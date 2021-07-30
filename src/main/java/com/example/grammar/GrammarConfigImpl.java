package com.example.grammar;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 文法
 *
 * @author wsx
 */
public class GrammarConfigImpl implements GrammarConfig {
    /**
     * 产生式集合
     */
    private final Map<String, Set<Production>> productionsTable;

    private final Map<Production, Integer> productionIdMap;
    private final Production[] productions;

    private final Set<String> terminal;

    private final Map<String, Integer> symbolIdMap;
    private final String[] symbols;

    /**
     * 文法目标
     */
    private final String target;

    private final String name;


    public GrammarConfigImpl(Map<String, Set<Production>> productionsTable, String target, Set<String> terminal, String name) {
        this.productionsTable = productionsTable;
        this.target = target;
        this.terminal = terminal.stream().collect(Collectors.toUnmodifiableSet());
        this.name = name;

        productions = productionsTable.values().stream()
                .flatMap(Collection::stream).toArray(Production[]::new);
        productionIdMap = IntStream.range(0, productions.length).boxed().collect(Collectors.toMap(i -> productions[i], i -> i));


        final List<String> symbolList = productionsTable.values()
                .stream()
                .flatMap(Collection::stream)
                .map(Production::leftSymbol)
                .distinct()
                .collect(Collectors.toList());
        symbolList.addAll(terminal);
        symbols = symbolList.toArray(new String[0]);
        symbolIdMap = IntStream.range(0, symbols.length).boxed().collect(Collectors.toMap(i -> symbols[i], i -> i));

        checkGrammar();
    }

    private void checkGrammar() {
        // 检验是否为产生式右边只能包含非终结符和终结符
        final Set<String> symbolsSet = Arrays.stream(symbols).collect(Collectors.toSet());
        productionsTable.values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(production -> {
                    if (production.rightSymbol().stream().anyMatch(o -> !symbolsSet.contains(o))) {
                        throw new RuntimeException("[%s] contain unknown symbol".formatted(production.toString()));
                    }
                });
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Set<String> allTerminal() {
        return terminal;
    }

    @Override
    public Set<String> allNotTerminal() {
        return productionsTable.values()
                .stream()
                .flatMap(Collection::stream)
                .map(Production::leftSymbol)
                .collect(Collectors.toSet());
    }

    @Override
    public Production[] allProduction() {
        return productions;
    }

    @Override
    public Set<Production> allProduction(String left) {
        return productionsTable.get(left).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Integer symbolId(String symbol) {
        return symbolIdMap.get(symbol);
    }

    @Override
    public Map<String, Integer> symbolIdMap() {
        return symbolIdMap;
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
    public String[] allSymbol() {
        return symbols;
    }

    @Override
    public String target() {
        return target;
    }

    @Override
    public boolean isTerminal(String symbol) {
        return terminal.contains(symbol);
    }
}
