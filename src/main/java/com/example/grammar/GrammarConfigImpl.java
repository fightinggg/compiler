package com.example.grammar;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final Set<String> terminal;

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
    public Set<Production> allProduction() {
        return productionsTable.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Production> allProduction(String left) {
        return productionsTable.get(left).stream().collect(Collectors.toUnmodifiableSet());
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
