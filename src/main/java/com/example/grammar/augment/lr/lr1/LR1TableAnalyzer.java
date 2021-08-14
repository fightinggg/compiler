package com.example.grammar.augment.lr.lr1;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFollowSet;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.utils.TableUtils;
import com.example.visiable.ProductionItemSetVisiable;
import com.example.visiable.FileUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
public class LR1TableAnalyzer implements LRTableAnalyzer {

    private static int order(Production production, String action) {
        if (action.equals(LRTable.Action.REDUCE) && production.leftCombination()) {
            return production.order();
        }
        if (action.equals(LRTable.Action.SHIFT) && !production.leftCombination()) {
            return production.order();
        }
        return production.order() + 1;
    }

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
        Map<Set<LR1AugmentProduction>, Map<Integer, Set<LR1AugmentProduction>>> itemSetDfa = LR1AugmentProductionItem.itemSetDfa(grammarConfig);
        List<Set<LR1AugmentProduction>> itemSetList = itemSetDfa.keySet().stream().toList();
        Map<Set<LR1AugmentProduction>, Integer> itemId = IntStream.range(0, itemSetDfa.size())
                .mapToObj(i -> Map.entry(itemSetList.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int row = itemSetDfa.size();
        int col = symbol.size();
        Integer[][] gotoTable = new Integer[row][col];

        @Getter
        @EqualsAndHashCode(callSuper = true)
        class OrderAction extends LRTable.Action {
            private final Integer order;

            public OrderAction(String ac, Integer jump, Integer order) {
                super(ac, jump);
                this.order = order;
            }

            @Override
            public String toString() {
                String orderString = order.toString();
                if (order.equals(Production.DEFAULT_ORDER)) {
                    orderString = "default";
                } else if (order.equals(Production.DEFAULT_ORDER + 1)) {
                    orderString = "default+1";
                }
                return "%s(%s)".formatted(super.toString(), orderString);
            }
        }
        Set<OrderAction>[][] actionTable = new Set[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                actionTable[i][j] = new HashSet<>();
            }
        }

        FileUtils.writeFile("target/%s-lr1-itemset.dot".formatted(grammarConfig.name()), ProductionItemSetVisiable.toDot(itemSetDfa, itemId, grammarConfig));
        FileUtils.writeFile("target/%s-lr1-itemset.txt".formatted(grammarConfig.name()), ProductionItemSetVisiable.toTxt(itemSetDfa, itemId));

        // 2.
        itemSetDfa.forEach((itemSet, trans) -> {
            Integer currentId = itemId.get(itemSet);

            itemSet.forEach(production -> {
                int pos = production.pos();
                Integer leftSymbol = production.leftSymbol();
                List<Integer> rightSymbol = production.rightSymbol();

                if (pos != rightSymbol.size() && grammarConfig.isTerminal(rightSymbol.get(pos))) {
                    // ①
                    Integer terminal = rightSymbol.get(pos);
                    OrderAction action = new OrderAction(LRTable.Action.SHIFT, itemId.get(trans.get(terminal)), order(production, LRTable.Action.SHIFT));
                    actionTable[currentId][terminal].add(action);
                } else if (pos == rightSymbol.size() && !leftSymbol.equals(grammarConfig.target())) {
                    // ②
                    OrderAction action = new OrderAction(LRTable.Action.REDUCE, grammarConfig.productionId(new ProductionImpl(production)), order(production, LRTable.Action.REDUCE));
                    // 这一行和slr不一样了， slr是选择所有的followset
                    production.next().stream().filter(o -> !o.equals(grammarConfig.emptyTerminal())).forEach(o -> actionTable[currentId][o].add(action));
                } else if (pos == rightSymbol.size() && leftSymbol.equals(grammarConfig.target()) && production.next().contains(grammarConfig.endTerminal())) {
                    // ③
                    actionTable[currentId][grammarConfig.endTerminal()].add(new OrderAction(LRTable.Action.ACC, 0, production.order()));
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

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                actionTable[i][j] = actionTable[i][j].stream()
                        .collect(Collectors.groupingBy(o -> new LRTable.Action(o.getAc(), o.getJump())))
                        .values().stream()
                        .flatMap(o -> o.stream().min(Comparator.comparing(OrderAction::getOrder)).stream())
                        .collect(Collectors.toSet());
            }
        }


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
        FileUtils.writeFile("target/%s-lr1table.tsv".formatted(grammarConfig.name()), TableUtils.tableToString(table, null, symbol));


        LRTable.Action[][] simpleActionTable = new LRTable.Action[row][col];

        boolean conflict = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int min = actionTable[i][j].stream().mapToInt(OrderAction::getOrder).min().orElse(Production.DEFAULT_ORDER);
                actionTable[i][j] = actionTable[i][j].stream().filter(o -> o.getOrder().equals(min)).collect(Collectors.toSet());
                if (actionTable[i][j].size() > 1) {
                    conflict = true;
                    System.out.printf(
                            "'state: %-3d'  + 'symbol: %-10s' Conflict: %s%n"
                            , i
                            , grammarConfig.symbol().get(j)
                            , actionTable[i][j].stream().map(Objects::toString).collect(Collectors.joining(" ")));
                } else if (actionTable[i][j].size() == 1) {
                    simpleActionTable[i][j] = actionTable[i][j].iterator().next();
                }
            }
        }
        if (conflict) {
            throw new RuntimeException("grammar %s is not lr1 grammar".formatted(grammarConfig.name()));
        }


        return new LRTable(gotoTable, simpleActionTable, grammarConfig.allProduction(), symbol.toArray(new String[0]), itemId.get(LR1AugmentProductionItem.begin(grammarConfig)));
    }
}
