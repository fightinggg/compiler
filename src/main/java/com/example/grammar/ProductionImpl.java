package com.example.grammar;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class ProductionImpl implements Production {
    private final Integer left;
    private final List<Integer> right;
    private final String raw;


    public ProductionImpl(Production production) {
        this(production.leftSymbol(), production.rightSymbol(), production.raw());
    }

    public ProductionImpl(Integer left, List<Integer> right, String raw) {
        this.left = left;
        // 不可变对象
        this.right = right.stream().toList();
        this.raw = raw;
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
        return raw;
    }

    @Override
    public String toString() {
        return raw;
    }
}
