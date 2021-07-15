package grammar;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author s
 */
public class GrammarOptimizer {

    Boolean findCanBeEmpty(Map<String,List<Production>> table,Set<String> canBeEmpty,Set<String> canNotEmpty,String target){
        if(canBeEmpty.contains(target)){
            return true;
        }
        if(canNotEmpty.contains(target)){
            return false;
        }
        Boolean res = false;
        if(target.length()<=1){
            res = target.isEmpty();
        }else{
            for(Production production:table.get(target)){
                if(production.getDerive().stream().allMatch(o->findCanBeEmpty(table,canBeEmpty,canNotEmpty,o))){
                    res=true;
                    break;
                }
            }
        }
        if(res){
            canBeEmpty.add(target);
        }else{
            canNotEmpty.add(target);
        }
        return res;
    }


    public static Grammar optimizer(Grammar grammar) {
        Map<String, List<Production>> productionsTable = grammar.getProductionsTable();

        // step.1 remove empty pattern
        Set<String> canBeEmpty = new HashSet<>();
        canBeEmpty


        // step.2 remove left dfs
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

        return null;

    }
}
