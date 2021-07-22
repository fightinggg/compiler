package com.example.grammar.augment.lr.slr;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFollowSet;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 1. 构造文法的规范LR(0) 项集簇 C = {I0, I1, ... In}.<br/>
 * 2. 对于状态i，<br/>
 * &emsp; ① 如果[A->α·aβ]在状态中，a为终结符且GOTO(i,a)=j,<br/>
 * &emsp; &emsp; 则ACTION[i,a]为: "移入j"<br/>
 * &emsp; ② 如果[A->α·]在状态中且A不为开始符号<br/>
 * &emsp; &emsp; 则对于所有的FOLLOW(A)的字符a，将ACTION[i,a]设为: "规约A->α"<br/>
 * &emsp; ③ 如果[A->α·]在状态中且A为开始符号<br/>
 * &emsp; &emsp; 则将ACTION[i,$]设为: "接受"<br/>
 */
public class SLRTableAnalyzer implements LRTableAnalyzer {

    @Override
    public LRTable analyze(GrammarConfig grammarConfig) {
        Map<String, Set<String>> followSet = GrammarFollowSet.followSet(grammarConfig);

        Map<Set<SLRAugmentProduction>, Map<String, Set<SLRAugmentProduction>>> itemSetDfa = SLRAugmentProductionItem.itemSetDfa(grammarConfig);
        Map<Set<SLRAugmentProduction>, Integer> itemId = new HashMap<>();
        final int[] idBegin = {0};
        itemSetDfa.keySet().forEach(slrAugmentProductions -> itemId.put(slrAugmentProductions, idBegin[0]++));


        List<Map<String, Integer>> gotoTable = itemSetDfa.keySet().stream()
                .map(o -> new HashMap<String, Integer>()).collect(Collectors.toList());
        List<Map<String, LRTable.Action>> actionTable = itemSetDfa.keySet().stream()
                .map(o -> new HashMap<String, LRTable.Action>()).collect(Collectors.toList());

        itemSetDfa.entrySet().forEach(kv -> {
            Set<SLRAugmentProduction> itemSet = kv.getKey();
            Map<String, Set<SLRAugmentProduction>> trans = kv.getValue();
            Integer currentId = itemId.get(itemSet);


            itemSet.forEach(slrAugmentProduction -> {
                int pos = slrAugmentProduction.pos();
                List<String> rightSymbol = slrAugmentProduction.rightSymbol();

                // ①
                if (pos != rightSymbol.size() && grammarConfig.isTerminal(rightSymbol.get(pos))) {
                    String terminal = rightSymbol.get(pos);
                    gotoTable.get(currentId).put(terminal, itemId.get(trans.get(terminal)));
                }
            });
        });


        return new LRTable(gotoTable, actionTable);
    }
}
