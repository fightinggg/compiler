package com.example.grammar.augment.lr.slr;

import com.alibaba.fastjson.JSON;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class SLRAugmentProductionImpl extends ProductionImpl implements SLRAugmentProduction {
    private final int pos;

    public SLRAugmentProductionImpl(Production production) {
        this(production, 0);
    }

    public SLRAugmentProductionImpl(Production production, int pos) {
        this(production.leftSymbol(), production.rightSymbol(), pos, production.raw());
    }

    public SLRAugmentProductionImpl(Integer left, List<Integer> right, String raw) {
        this(left, right, 0, raw);
    }


    public SLRAugmentProductionImpl(Integer left, List<Integer> right, int pos, String raw) {
        super(left, right, raw);
        this.pos = pos;
    }


    @Override
    public int pos() {
        return pos;
    }


    @Override
    public String toString() {
        List<String> strings = new ArrayList<>(rightSymbol()).stream().map(Object::toString).collect(Collectors.toList());
        strings.add(pos, "·");
        String s = JSON.toJSONString(String.join(" ", strings));
        return "%s -> %s".formatted(leftSymbol(), s.substring(1, s.length() - 1));
    }
}
