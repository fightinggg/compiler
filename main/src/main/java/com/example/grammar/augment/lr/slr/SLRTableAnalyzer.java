package com.example.grammar.augment.lr.slr;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFollowSet;
import com.example.grammar.ProductionImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.utils.TableUtils;
import com.example.visiable.ProductionItemSetVisiable;
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
        Map<Integer, Set<Integer>> followSet = GrammarFollowSet.followSet(grammarConfig);
        List<String> symbol = grammarConfig.symbol();

        final String followSetTxt = followSet.entrySet().stream()
                .map(kv -> symbol.get(kv.getKey()) + " :  " +
                        kv.getValue().stream().map(symbol::get).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
        FileUtils.writeFile("target/%s-followset.txt".formatted(grammarConfig.name()), followSetTxt);

        // 1.
        Map<Set<SLRAugmentProduction>, Map<Integer, Set<SLRAugmentProduction>>> itemSetDfa = SLRAugmentProductionItem.itemSetDfa(grammarConfig);
        List<Set<SLRAugmentProduction>> itemSetList = itemSetDfa.keySet().stream().toList();
        Map<Set<SLRAugmentProduction>, Integer> itemId = IntStream.range(0, itemSetDfa.size())
                .mapToObj(i -> Map.entry(itemSetList.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int row = itemSetDfa.size();
        int col = symbol.size();
        Integer[][] gotoTable = new Integer[row][col];
        Set<LRTable.Action>[][] actionTable = new Set[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                actionTable[i][j] = new HashSet<>();
            }
        }

        FileUtils.writeFile("target/%s-slr-itemset.dot".formatted(grammarConfig.name()), ProductionItemSetVisiable.toDot(itemSetDfa, itemId, grammarConfig));
        FileUtils.writeFile("target/%s-slr-itemset.txt".formatted(grammarConfig.name()), ProductionItemSetVisiable.toTxt(itemSetDfa, itemId));

        // 2.
        itemSetDfa.forEach((itemSet, trans) -> {
            Integer currentId = itemId.get(itemSet);

            itemSet.forEach(slrAugmentProduction -> {
                int pos = slrAugmentProduction.pos();
                Integer leftSymbol = slrAugmentProduction.leftSymbol();
                List<Integer> rightSymbol = slrAugmentProduction.rightSymbol();

                if (pos != rightSymbol.size() && grammarConfig.isTerminal(rightSymbol.get(pos))) {
                    // ①
                    Integer terminal = rightSymbol.get(pos);
                    LRTable.Action action = new LRTable.Action("s", itemId.get(trans.get(terminal)));
                    actionTable[currentId][terminal].add(action);
                } else if (pos == rightSymbol.size() && !leftSymbol.equals(grammarConfig.target())) {
                    // ②
                    LRTable.Action action = new LRTable.Action("r", grammarConfig.productionId(new ProductionImpl(slrAugmentProduction)));
                    followSet.get(leftSymbol).stream().filter(o -> !o.equals(grammarConfig.emptyTerminal())).forEach(o -> actionTable[currentId][o].add(action));
                } else if (pos == rightSymbol.size() && leftSymbol.equals(grammarConfig.target())) {
                    // ③
                    actionTable[currentId][grammarConfig.endTerminal()].add(new LRTable.Action(LRTable.Action.ACC, 0));
                }
            });
        });

        // 3.
        itemSetDfa.forEach((itemSet, trans) -> {
            Integer currentId = itemId.get(itemSet);

            trans.keySet().stream()
                    .filter(o -> !grammarConfig.isTerminal(o))
                    .forEach(nonTerminal -> gotoTable[currentId][nonTerminal] = itemId.get(trans.get(nonTerminal)));
        });


        // 把actionTable转化为二维的表格
        final List<List<String>> table = Arrays.stream(actionTable)
                .map(o -> Arrays.stream(o).map(list -> list.stream().map(Objects::toString).collect(Collectors.joining(","))).collect(Collectors.toList()))
                .toList();
        // 复写gotoTable
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (!grammarConfig.isTerminal(j)) {
                    String goTo = gotoTable[i][j] == null ? "" : gotoTable[i][j].toString();
                    table.get(i).set(j, goTo);
                }
            }
        }
        List<List<String>> productionListTable = Arrays.stream(grammarConfig.allProduction()).map(Objects::toString).map(List::of).toList();
        FileUtils.writeFile("target/%s-production.tsv".formatted(grammarConfig.name()), TableUtils.tableToString(productionListTable, null, null));
        FileUtils.writeFile("target/%s-slrtable.tsv".formatted(grammarConfig.name()), TableUtils.tableToString(table, null, symbol));


        LRTable.Action[][] simpleActionTable = new LRTable.Action[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (actionTable[i][j].size() > 1) {
                    throw new RuntimeException("grammar %s is not slr grammar".formatted(grammarConfig.name()));
                } else if (actionTable[i][j].size() == 1) {
                    simpleActionTable[i][j] = actionTable[i][j].iterator().next();
                }
            }
        }


        return new LRTable(gotoTable, simpleActionTable, grammarConfig.allProduction(), symbol.toArray(new String[0]), itemId.get(SLRAugmentProductionItem.begin(grammarConfig)));
    }
}
