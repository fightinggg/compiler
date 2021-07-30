package com.example.grammar;

import java.util.*;
import java.util.stream.Collectors;

public class GrammarFirstSet {


    /**
     * 1. 如果X是终结符，则FIRST(X) = X<br/>
     * 2. X->ABCDE... 是一个产生式， 则FIRST(X)=FIRST(A), 如果此时A可为空，则把FIRST(B)加入到FIRST(X)，依次递推<br/>
     * 3. 如果X可以为空，则FIRST(X)也包含空<br/>
     * <br/>
     *
     * @param grammarConfig 文法
     * @return first集
     */
    public static Map<String, Set<String>> firstSet(GrammarConfig grammarConfig) {
        // 获取产生式集合
        Set<Production> productions = Arrays.stream(grammarConfig.allProduction()).collect(Collectors.toUnmodifiableSet());

        // 寻找可以为空的非终极符
        Set<String> emptySet = GrammarEmptySet.emptySet(grammarConfig);

        // 空first入队
        Queue<Map.Entry<String, String>> firstSetQueue = emptySet.stream().map(o -> Map.entry(o, "")).distinct().collect(Collectors.toCollection(ArrayDeque::new));

        // 终结符入队
        Set<String> allTerminal = grammarConfig.allTerminal();
        firstSetQueue.addAll(allTerminal.stream().map(o -> Map.entry(o, o)).collect(Collectors.toSet()));

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
        Map<String, Set<String>> firstSet = new HashMap<>();

        // 依赖松弛
        while (!firstSetQueue.isEmpty()) {
            // k 为 非终结符
            // v 为 first集中的一个
            Map.Entry<String, String> release = firstSetQueue.poll();
            dependsBy.getOrDefault(release.getKey(), new ArrayList<>())
                    .forEach(dependsKv -> {
                        firstSet.putIfAbsent(dependsKv.getKey(), new HashSet<>());
                        if (firstSet.get(dependsKv.getKey()).add(release.getValue())) {
                            firstSetQueue.add(Map.entry(dependsKv.getKey(), release.getValue()));
                        }
                    });
        }

        emptySet.forEach(o -> firstSet.putIfAbsent(o, new HashSet<>()));
        emptySet.forEach(o -> firstSet.get(o).add(""));
        allTerminal.forEach(o -> firstSet.putIfAbsent(o, new HashSet<>()));
        allTerminal.forEach(o -> firstSet.get(o).add(o));

        return firstSet;
    }
}
