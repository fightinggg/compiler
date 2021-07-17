package syntaxtree;

import grammar.NormalGrammar;
import grammar.Production;
import lombok.Data;

import java.util.List;

/**
 * 语法树
 *
 * @author wsx
 */
@Data
public class SyntaxTree {
    @Data
    public static class Node {
        private String raw;

        private Production production;
        private List<Node> son;
    }

    private NormalGrammar normalGrammar;
    private Node root;
}
