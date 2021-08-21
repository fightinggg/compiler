package com.example.grammar.augment.lr.slr;

import com.alibaba.fastjson.JSON;
import com.example.grammar.GrammarConfig;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class SLRAugmentProductionImpl extends ProductionImpl implements SLRAugmentProduction {
    private final int pos;

    public SLRAugmentProductionImpl(Production production) {
        this(production, 0);
    }

    public SLRAugmentProductionImpl(Production production, int pos) {
        this(production.leftSymbol(), production.rightSymbol(), production.order(), production.leftCombination(), pos, production.grammarConfig());
    }

    public SLRAugmentProductionImpl(Integer left, List<Integer> right, int order, boolean leftCombination, int pos, GrammarConfig grammarConfig) {
        super(left, right, order, leftCombination, grammarConfig);
        this.pos = pos;
    }

    @Override
    public int pos() {
        return pos;
    }

    @Override
    public String toString() {
        List<String> strings = rightSymbol().stream().map(grammarConfig().symbol()::get).collect(Collectors.toList());
        strings.add(pos, "·");
        String s = JSON.toJSONString(String.join(" ", strings));
        return "%s -> %s".formatted(raw().split("->")[0], s.substring(1, s.length() - 1));
    }
}
