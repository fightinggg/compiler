package grammar;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GrammarFirstSet {


    public static Map<String, Set<String>> firstSet(Grammar grammar) {
        // 获取产生式集合
        Set<Production> productions = grammar.allProduction();

        // 寻找可以为空的非终极符
        Set<String> emptySet = GrammarEmptySet.emptySet(grammar);

        Queue<Map.Entry<String,String>> firstSetQueue = new ArrayDeque<>();

        // 空first入队
        firstSetQueue.addAll(emptySet.stream().map(o->Map.entry(o,"")).collect(Collectors.toSet()));
        // 终结符入队
        firstSetQueue.addAll(grammar.allTerminal().stream().map(o->Map.entry(o,o)).collect(Collectors.toSet()));

        List<Map.Entry<String, String>> allDepends = productions.stream()
                .filter(o -> !o.rightSymbol().isEmpty())
                .flatMap(o -> {
                    List<String> strings = o.rightSymbol();
                    Set<Map.Entry<String, String>> depends = new HashSet<>();
                    depends.add(Map.entry(o.leftSymbol(), strings.get(0)));
                    for (int i = 0; i < strings.size() - 1; i++) {
                        if (emptySet.contains(strings.get(i))) {
                            depends.add(Map.entry(o.leftSymbol(), strings.get(i + 1)));
                        } else {
                            break;
                        }
                    }
                    return depends.stream();
                })
                .collect(Collectors.toList());

        // 逆向构造依赖图
        Map<String, List<Map.Entry<String, String>>> dependsBy = allDepends.stream().collect(Collectors.groupingBy(Map.Entry::getValue));

        // first集
        Map<String,Set<String>> firstSet = new HashMap<>();

        // 依赖松弛
        while(!firstSetQueue.isEmpty()){
            // k 为 非终结符
            // v 为 first集中的一个
            Map.Entry<String,String> release = firstSetQueue.poll();
            dependsBy.get(release.getKey()).stream()
                    .forEach(dependsKv -> {
                        if (firstSet.get(dependsKv.getKey()).add(release.getValue())) {
                            firstSetQueue.add(Map.entry(dependsKv.getKey(), release.getValue()));
                        }
                    });
        }

        return firstSet;
    }
}
