package com.example.grammar;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class ProductionImpl implements Production {
    private final String left;
    private final List<String> right;


    public ProductionImpl(String productionString) {
        this(productionString.split("->")[0].strip(),
                Arrays.stream(productionString.split("->")[1].split(" ")).filter(o -> !o.isBlank()).toList());
    }

    public ProductionImpl(Production production) {
        this.left = production.leftSymbol();
        // 不可变对象
        this.right = production.rightSymbol();
    }

    public ProductionImpl(String left, List<String> right) {
        this.left = left;
        // 不可变对象
        this.right = right.stream().toList();
    }


    @Override
    public String leftSymbol() {
        return left;
    }

    @Override
    public List<String> rightSymbol() {
        return right;
    }

    @Override
    public String toString() {
        return left + " -> " + String.join(" ", right);
    }
}
