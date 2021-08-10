package com.example.grammar;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class ProductionImpl implements Production {
    private final Integer left;
    private final List<Integer> right;

    @EqualsAndHashCode.Exclude
    private final GrammarConfig grammarConfig;


    public ProductionImpl(Production production) {
        this(production.leftSymbol(), production.rightSymbol(), production.grammarConfig());
    }

    public ProductionImpl(Integer left, List<Integer> right, GrammarConfig grammarConfig) {
        this.left = left;
        // 不可变对象
        this.right = right.stream().toList();
        this.grammarConfig = grammarConfig;

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
    public GrammarConfig grammarConfig() {
        return grammarConfig;
    }

    @Override
    public String toString() {
        return raw();
    }
}
