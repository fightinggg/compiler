package com.example.grammar.augment.lr.lr1;

import com.example.grammar.Production;

import java.util.List;

public interface LR1AugmentProduction extends Production {
    int pos();

    List<Integer> next();
}
