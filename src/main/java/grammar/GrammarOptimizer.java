package grammar;

import java.util.List;
import java.util.Map;

/**
 * @author s
 */
public class GrammarOptimizer {
    public static Grammar optimizer(Grammar grammar) {
        Map<String, List<Production>> productionsTable = grammar.getProductionsTable();

        // step.1 消除左递归
        // A -> B??
        // B -> C??
        // C -> D??
        // D -> A??
        // 优化为
        // A -> B??
        // B -> C??
        // C -> D??
        // D -> D??????


        // step.2 左公因式提取

        return null;

    }
}
