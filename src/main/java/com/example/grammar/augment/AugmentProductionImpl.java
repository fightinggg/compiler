package com.example.grammar.augment;

import com.alibaba.fastjson.JSON;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class AugmentProductionImpl extends ProductionImpl implements AugmentProduction {
    private final int pos;

    public AugmentProductionImpl(Production production) {
        this(production, 0);
    }

    public AugmentProductionImpl(Production production, int pos) {
        this(production.leftSymbol(), production.rightSymbol(), pos);
    }

    public AugmentProductionImpl(String left, List<String> right) {
        this(left, right, 0);
    }


    public AugmentProductionImpl(String left, List<String> right, int pos) {
        super(left, right);
        this.pos = pos;
    }


    @Override
    public int pos() {
        return pos;
    }


    @Override
    public String toString() {
        ArrayList<String> strings = new ArrayList<>(rightSymbol());
        strings.add(pos, "·");
        String s = JSON.toJSONString(String.join(" ", strings));
        return "%s -> %s".formatted(leftSymbol(), s.substring(1, s.length() - 1));
    }
}
