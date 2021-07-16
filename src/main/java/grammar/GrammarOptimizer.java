package grammar;

import java.util.List;
import java.util.Map;

/**
 * @author s
 */
public class GrammarOptimizer {

    public static Grammar optimizer(Grammar grammar) {
        Map<String, List<Production>> productionsTable = grammar.getProductionsTable();

        // step.1 remove empty pattern


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


