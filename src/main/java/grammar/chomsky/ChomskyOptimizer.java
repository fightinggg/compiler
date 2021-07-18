package grammar.chomsky;


import grammar.Grammar;
import grammar.NormalProduction;
import grammar.Production;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 乔姆斯基范式
 * https://zh.wikipedia.org/wiki/%E4%B9%94%E5%A7%86%E6%96%AF%E5%9F%BA%E8%8C%83%E5%BC%8F
 */
public class ChomskyOptimizer {
    public static ChomskyGrammar optimizer(Grammar<Production> grammar) {
        String prefix = "ChomskyOptimizer";
        Integer index = 0;

        Set<Production> productions = new HashSet<>();
        for (Production production : grammar.allProduction()) {
            Deque<String> strings = new ArrayDeque<>(production.rightSymbol());
            while (strings.size() > 2) {
                String first1 = strings.pollFirst();
                String first2 = strings.pollFirst();

                String tmp = prefix + "_" + (++index);
                productions.add(new NormalProduction(tmp,List.of(first1,first2)));

                strings.addFirst(tmp);
            }
            productions.add(new NormalProduction(production.leftSymbol(),new ArrayList<>(strings)));
        }


        // 去空
        while(true){
            Set<String> emtpy = productions.stream()
                    .filter(o -> o.rightSymbol().isEmpty())
                    .map(Production::leftSymbol)
                    .collect(Collectors.toSet());
            if(emtpy.isEmpty()){
                break;
            }
            productions = productions.stream()
                    .filter(o->!o.rightSymbol().isEmpty())
                    .flatMap(production -> {
                        String left = production.leftSymbol();
                        List<String> strings = production.rightSymbol();
                        List<Production> res= new ArrayList<>();
                        res.add(production);

                        if(strings.size()==1){
                            if(emtpy.contains(strings.get(0))){
                                res.add(new NormalProduction(left,new ArrayList<>()));
                            }
                        }else{
                            if(emtpy.contains(strings.get(0))&&emtpy.contains(strings.get(1))){
                                res.add(new NormalProduction(left,new ArrayList<>()));
                                res.add(new NormalProduction(left,List.of(strings.get(0))));
                                res.add(new NormalProduction(left,List.of(strings.get(1))));
                            }else if(emtpy.contains(strings.get(0))){
                                res.add(new NormalProduction(left,List.of(strings.get(1))));
                            }else if(emtpy.contains(strings.get(1))){
                                res.add(new NormalProduction(left,List.of(strings.get(0))));
                            }
                        }
                        return res.stream();
                    })
                    .collect(Collectors.toSet());
        }

        // 往下递推
        // A -> B C
        // B -> D1 D2 | D3 D4 | ...
        // 展开B
        // A -> D1 (D2 C) | D3

        // 消除直接左递归
        // A -> A B | D1 D2 | D3 D4 | D5 D6 | ...
        // 引入A'
        // A' -> D1 D2 | D3 D4 | D5 D6 | ...
        // A -> A' someB
//        for(Production production:productions){
//            if(production)
//        }
        return null;
    }
}
