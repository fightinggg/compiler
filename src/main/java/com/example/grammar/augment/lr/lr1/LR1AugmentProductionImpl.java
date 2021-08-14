package com.example.grammar.augment.lr.lr1;

import com.alibaba.fastjson.JSON;
import com.example.grammar.GrammarConfig;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import com.example.grammar.augment.lr.slr.SLRAugmentProduction;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class LR1AugmentProductionImpl extends ProductionImpl implements LR1AugmentProduction {
    private final int pos;
    private final Set<Integer> next;


    public LR1AugmentProductionImpl(Production production, int pos, List<Integer> next) {
        this(production.leftSymbol(), production.rightSymbol(), production.order(), production.leftCombination(), pos, next, production.grammarConfig());
    }

    public LR1AugmentProductionImpl(Integer left, List<Integer> right, int order, boolean leftCombination, int pos, List<Integer> next, GrammarConfig grammarConfig) {
        super(left, right, order, leftCombination, grammarConfig);
        this.pos = pos;
        this.next = Set.copyOf(next);
    }

    @Override
    public int pos() {
        return pos;
    }

    @Override
    public List<Integer> next() {
        return next.stream().toList();
    }

    @Override
    public String toString() {
        List<String> strings = Arrays.stream(raw().split("->")[1].split(" ")).filter(o -> !o.equals("")).collect(Collectors.toList());
        strings.add(pos, "·");
        return "%s -> %s [%s]".formatted(raw().split("->")[0], String.join(" ", strings), next.stream().map(grammarConfig().symbol()::get).collect(Collectors.joining(",")));
    }

}
