package grammar;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author s
 */
public class GrammarOptimizer {

    /**
     * n = 非终结符的个数
     * m = 产生式的总数
     * k = 产生式右边的长度
     * 则时间复杂度为 O(n*m*2^k)
     *
     * @param normalGrammar
     */
    static void removeEmtpy(NormalGrammar normalGrammar) {
        Map<String, Set<List<String>>> productionsTable = normalGrammar.getProductionsTable();
        boolean hasEmptyTrans = true;
        while (hasEmptyTrans) {
            Set<String> emptySet = new HashSet<>();
            for (Map.Entry<String, Set<List<String>>> pair : productionsTable.entrySet()) {
                if (pair.getValue().removeIf(production -> production.isEmpty())) {
                    emptySet.add(pair.getKey());
                }
            }

            for (Map.Entry<String, Set<List<String>>> pair : productionsTable.entrySet()) {
                Set<List<String>> addProductions = new HashSet<>();
                for (List<String> production : pair.getValue()) {
                    int mask = 0;
                    for (int i = 0; i < production.size(); i++) {
                        if (emptySet.contains(production.get(i))) {
                            mask |= 1 << i;
                        }
                    }
                    if (mask == 0) {
                        continue;
                    }
                    for (int subSet = mask; subSet > 0; subSet = (subSet - 1) & mask) {
                        List<String> dervice = new ArrayList<>();
                        for (int i = 0; i < production.size(); i++) {
                            if ((subSet & (1 << i)) == 0) {
                                dervice.add(production.get(i));
                            }
                        }
                        List<String> addList<String> = new List<String>();
                        addList<String>.setFrom(pair.getKey());
                        addList<String>.setDerive(dervice);
                        addProductions.add(addList<String>);
                    }
                }
                pair.getValue().addAll(addProductions);
            }
            hasEmptyTrans = !emptySet.isEmpty();
        }
    }

    private static void removeLeftRecursion(NormalGrammar normalGrammar) {
        final String prefix = "REMOVE_LEFT_RECURSION";
        int index = 0;

        Map<String, Set<List<String>>> productionsTable = normalGrammar.getProductionsTable();

        Set<String> hasScan = new HashSet<>();
        for (Map.Entry<String, Set<List<String>>> pair : productionsTable.entrySet()) {

            Stack<List<String>> collect = pair.getValue().stream().collect(Collectors.toCollection(Stack::new));
            Set<List<String>> newList<String>s = new HashSet<>();
            productionsTable.remove(pair.getKey());

            while (!collect.isEmpty()) {
                List<String> top = collect.pop();
                String first = top.getDerive().get(0);
                if (hasScan.contains(first)) {

                } else if (first.equals(top.getFrom())) {
                    List<String> currentLoop = top.getDerive();
                    // A -> A B1 B2 B3... | C1 C2 C3... | D1 D2 D3... | E1 E2 E3... | ...

                    // oldA -> C1 C2 C3... | D1 D2 D3... | E1 E2 E3... | ...
                    // B -> B1 B2 B3...
                    // A -> A B | oldA


                    // oldA -> C1 | C2 | C3 | ...
                    // atLeastOneB -> B | B atLeastOneB
                    // A -> oldA | oldA atLeastOneB


                    String oldA = prefix + "_" + (++index);
                    String B = prefix + "_" + (++index);
                    String A = top.getFrom();

                    String bDerive = String.join(" ",currentLoop.subList(1, currentLoop.size()));
                    productionsTable.put(B, List<String>Factory.fromStringSet(B, Set.of(bDerive)));

                    productionsTable.put(A, List<String>Factory.fromStringSet(A, Set.of(A + " " + B, oldA)));



                } else {
                    newList<String>s.add(top);
                }
            }

            productionsTable.put(pair.getKey(), newList<String>s);
            hasScan.add(pair.getKey());
        }
    }


}


    public static void optimizer(NormalGrammar normalGrammar) {
        // step.1 remove empty pattern
        removeEmtpy(normalGrammar);


        // step.2 remove left recursion
        removeLeftRecursion(normalGrammar);


        // A -> B??
        // B -> C??
        // C -> D??
        // D -> A??
        // 优化为
        // A -> B??
        // B -> C??
        // C -> D??
        // D -> D??????


        // step.3 left same factor


    }
}


