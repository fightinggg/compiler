package com.example.grammar;

import com.example.lexical.Token;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
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

    private final Map<Production, Integer> productionIdMap;
    private final Production[] productions;

    private final List<Integer> terminal;
    private final List<Integer> nonTerminal;
    private final List<String> symbols;

    /**
     * 文法目标
     */
    private final Integer target;

    private final String name;
    private Integer emptyTerminal;
    private Integer endTerminal;


    public GrammarConfigImpl(Production[] productions, Integer target, List<Integer> terminal, List<Integer> nonTerminal, List<String> symbols, String name) {
        this.target = target;
        this.terminal = terminal;
        this.nonTerminal = nonTerminal;
        this.name = name;
        this.symbols = symbols;
        this.productions = productions;
        this.productionIdMap = IntStream.range(0, productions.length).boxed().collect(Collectors.toMap(i -> productions[i], i -> i));
        List<Integer> emptyTerminals = terminal.stream().filter(o -> symbols.get(o).equals("")).collect(Collectors.toList());
        if (emptyTerminals.size() != 1) {
            throw new RuntimeException("终结符中缺少 empty");
        }
        this.emptyTerminal = emptyTerminals.get(0);

        List<Integer> endTerminals = terminal.stream().filter(o -> symbols.get(o).equals(Token.END)).collect(Collectors.toList());
        if (endTerminals.size() != 1) {
            throw new RuntimeException("终结符中缺少 end");
        }
        this.endTerminal = endTerminals.get(0);
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
