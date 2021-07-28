package com.example.grammar.augment.lr.slr;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFollowSet;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.lexical.Token;
import com.example.visiable.AugmentProductionItemSetVisiable;
import com.example.visiable.FileUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 1. 构造文法的规范LR(0) 项集簇 C = {I0, I1, ... In}.<br/>
 * 2. 对于状态i，<br/>
 * &emsp; ① 如果[A->α·aβ]在状态中，a为终结符且GOTO(i,a)=j,<br/>
 * &emsp; &emsp; 则ACTION[i,a]为: "移入j"<br/>
 * &emsp; ② 如果[A->α·]在状态中且A不为开始符号<br/>
 * &emsp; &emsp; 则对于所有的FOLLOW(A)的字符a，将ACTION[i,a]设为: "规约A->α"<br/>
 * &emsp; ③ 如果[A->α·]在状态中且A为开始符号<br/>
 * &emsp; &emsp; 则将ACTION[i,$]设为: "接受"<br/>
 * 3. 对于状态i，如果A为非终结符且 GOTO(i,A)=j 则GOTO[i,A]=j<br/>
 * 4. 剩下的地方就是报错<br/>
 * 5. 起点是[S'->·S]<br/>
 */
public class SLRTableAnalyzer implements LRTableAnalyzer {

    @Override
    public LRTable analyze(GrammarConfig grammarConfig) {
        Map<String, Set<String>> followSet = GrammarFollowSet.followSet(grammarConfig);
        List<Production> productions = grammarConfig.allProduction().stream().map(ProductionImpl::new).collect(Collectors.toList());
        Map<Production, Integer> productionId = IntStream.range(0, productions.size())
                .mapToObj(i -> Map.entry(productions.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 1.
        Map<Set<SLRAugmentProduction>, Map<String, Set<SLRAugmentProduction>>> itemSetDfa = SLRAugmentProductionItem.itemSetDfa(grammarConfig);
        List<Set<SLRAugmentProduction>> itemSetList = itemSetDfa.keySet().stream().toList();
        Map<Set<SLRAugmentProduction>, Integer> itemId = IntStream.range(0, itemSetDfa.size())
                .mapToObj(i -> Map.entry(itemSetList.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Map<String, Integer>> gotoTable = itemSetDfa.keySet().stream()
                .map(o -> new HashMap<String, Integer>()).collect(Collectors.toList());
        List<Map<String, LRTable.Action>> actionTable = itemSetDfa.keySet().stream()
                .map(o -> new HashMap<String, LRTable.Action>()).collect(Collectors.toList());

        FileUtils.writeFile("target/%s-slr-itemset.dot".formatted(grammarConfig.name()), AugmentProductionItemSetVisiable.toDot(itemSetDfa, itemId));
        FileUtils.writeFile("target/%s-slr-itemset.txt".formatted(grammarConfig.name()), AugmentProductionItemSetVisiable.toTxt(itemSetDfa, itemId));

        // 2.
        itemSetDfa.forEach((itemSet, trans) -> {
            Integer currentId = itemId.get(itemSet);

            itemSet.forEach(slrAugmentProduction -> {
                int pos = slrAugmentProduction.pos();
                String leftSymbol = slrAugmentProduction.leftSymbol();
                List<String> rightSymbol = slrAugmentProduction.rightSymbol();

                if (pos != rightSymbol.size() && grammarConfig.isTerminal(rightSymbol.get(pos))) {
                    // ①
                    String terminal = rightSymbol.get(pos);
                    LRTable.Action action = new LRTable.Action("s", itemId.get(trans.get(terminal)));
                    actionTable.get(currentId).put(terminal, action);
                } else if (pos == rightSymbol.size() && !leftSymbol.equals(grammarConfig.target())) {
                    // ②
                    LRTable.Action action = new LRTable.Action("r", productionId.get(new ProductionImpl(slrAugmentProduction)));
                    followSet.get(leftSymbol).forEach(o -> {
                        assert !actionTable.get(currentId).containsKey(o);
                    });
                    followSet.get(leftSymbol).forEach(o -> actionTable.get(currentId).put(o, action));
                } else if (pos == rightSymbol.size() && leftSymbol.equals(grammarConfig.target())) {
                    // ③
                    actionTable.get(currentId).put(Token.END, new LRTable.Action(LRTable.Action.ACC, 0));
                }
            });
        });

        // 3.
        itemSetDfa.forEach((itemSet, trans) -> {
            Integer currentId = itemId.get(itemSet);
            Map<String, Integer> currentGoto = gotoTable.get(currentId);

            trans.keySet().stream()
                    .filter(o -> !grammarConfig.isTerminal(o))
                    .forEach(nonTerminal -> currentGoto.put(nonTerminal, itemId.get(trans.get(nonTerminal))));
        });


        return new LRTable(gotoTable, actionTable, productions, itemId.get(SLRAugmentProductionItem.begin(grammarConfig)));
    }
}
