package com.example.grammar;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class ProductionImpl implements Production {
    private final String left;
    private final List<String> right;

    public ProductionImpl(String left, List<String> right){
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
}
