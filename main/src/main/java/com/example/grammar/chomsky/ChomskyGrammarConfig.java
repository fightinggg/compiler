//package com.example.grammar.chomsky;
//
//import com.example.grammar.GrammarConfig;
//import com.example.grammar.Production;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class ChomskyGrammarConfig implements GrammarConfig {
//    /**
//     * 产生式集合
//     */
//    private Map<String, Set<ChomskyProduction>> productionsTable;
//
//    /**
//     * 文法目标
//     */
//    private String target;
//
//    @Override
//    public String name() {
//        return null;
//    }
//
//    @Override
//    public Set<String> allTerminal() {
//        throw new RuntimeException();
//    }
//
//    @Override
//    public Set<String> allNotTerminal() {
//        throw new RuntimeException();
//    }
//
//    @Override
//    public Production[] allProduction() {
//        return null;
//    }
//
//    @Override
//    public Set<Production> allProduction(String left) {
//        return productionsTable.get(left).stream()
//                .collect(Collectors.toUnmodifiableSet());
//    }
//
//    @Override
//    public Integer symbolId(String symbol) {
//        return null;
//    }
//
//    @Override
//    public Map<String, Integer> symbolIdMap() {
//        return null;
//    }
//
//    @Override
//    public Integer productionId(Production production) {
//        return null;
//    }
//
//    @Override
//    public Map<Production, Integer> productionIdMap() {
//        return null;
//    }
//
//    @Override
//    public String target() {
//        return target;
//    }
//
//    @Override
//    public boolean isTerminal(String symbol) {
//        throw new RuntimeException();
//    }
//
//
//}
