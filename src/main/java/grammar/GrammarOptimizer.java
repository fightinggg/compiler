package grammar;

import com.sun.nio.file.ExtendedWatchEventModifier;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

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
     * @param grammar
     */
    static void removeEmtpy(Grammar grammar) {
        Map<String, Set<Production>> productionsTable = grammar.getProductionsTable();
        boolean hasEmptyTrans = true;
        while (hasEmptyTrans) {
            Set<String> emptySet = new HashSet<>();
            for (Map.Entry<String, Set<Production>> pair : productionsTable.entrySet()) {
                if (pair.getValue().removeIf(production -> production.getDerive().isEmpty())) {
                    emptySet.add(pair.getKey());
                }
            }

            for (Map.Entry<String, Set<Production>> pair : productionsTable.entrySet()) {
                Set<Production> addProductions = new HashSet<>();
                for (Production production : pair.getValue()) {
                    int mask = 0;
                    for (int i = 0; i < production.getDerive().size(); i++) {
                        if (emptySet.contains(production.getDerive().get(i))) {
                            mask |= 1 << i;
                        }
                    }
                    if (mask == 0) {
                        continue;
                    }
                    for (int subSet = mask; subSet > 0; subSet = (subSet - 1) & mask) {
                        List<String> dervice = new ArrayList<>();
                        for (int i = 0; i < production.getDerive().size(); i++) {
                            if ((subSet & (1 << i)) == 0) {
                                dervice.add(production.getDerive().get(i));
                            }
                        }
                        Production addProduction = new Production();
                        addProduction.setFrom(pair.getKey());
                        addProduction.setDerive(dervice);
                        addProductions.add(addProduction);
                    }
                }
                pair.getValue().addAll(addProductions);
            }
            hasEmptyTrans = !emptySet.isEmpty();
        }
    }

    private static void removeLeftRecursion(Grammar grammar) {
        Map<String, Set<Production>> productionsTable = grammar.getProductionsTable();

        Map<String, Set<String>> firstMap = new HashMap<>();

        for (Map.Entry<String, Set<Production>> pair : productionsTable.entrySet()) {
            HashSet<String> first = new HashSet<>();
            for (Production production : pair.getValue()) {
                String begin = production.getDerive().get(0);
                while (firstMap.containsKey(begin)) {
                    begin =
                }
            }

        }

    }


    public static void optimizer(Grammar grammar) {
        // step.1 remove empty pattern
        removeEmtpy(grammar);


        // step.2 remove left recursion
        removeLeftRecursion(grammar);


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


