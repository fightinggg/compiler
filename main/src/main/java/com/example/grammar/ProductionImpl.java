package com.example.grammar;

import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class ProductionImpl implements Production {
    private final Integer left;
    private final List<Integer> right;

    @EqualsAndHashCode.Exclude
    private final Integer order;
    @EqualsAndHashCode.Exclude
    private final Boolean leftCombination;
    @EqualsAndHashCode.Exclude
    private final GrammarConfig grammarConfig;


    public ProductionImpl(Production production) {
        this(production.leftSymbol(), production.rightSymbol(), production.order(), production.leftCombination(), production.grammarConfig());
    }

    public ProductionImpl(Integer left, List<Integer> right, Integer order, Boolean leftCombination, GrammarConfig grammarConfig) {
        this.left = left;
        // 不可变对象
        this.right = right.stream().toList();
        this.grammarConfig = grammarConfig;
        this.order = order;
        this.leftCombination = leftCombination;

        // check
        if (right.stream().anyMatch(Objects::isNull)) {
            throw new RuntimeException("error production");
        }
    }

    @Override
    public Integer leftSymbol() {
        return left;
    }

    @Override
    public List<Integer> rightSymbol() {
        return right;
    }

    @Override
    public String raw() {
        return "%s -> %s".formatted(grammarConfig.symbol().get(left), right.stream().map(grammarConfig.symbol()::get).collect(Collectors.joining(" ")));
    }

    @Override
    public Integer order() {
        return order;
    }

    @Override
    public Boolean leftCombination() {
        return leftCombination;
    }

    @Override
    public GrammarConfig grammarConfig() {
        return grammarConfig;
    }

    @Override
    public String toString() {
        return raw();
    }
}
