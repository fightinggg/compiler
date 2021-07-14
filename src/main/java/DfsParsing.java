import grammar.Grammar;
import grammar.Production;
import syntaxtree.SyntaxTree;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 递归语法分析器
 *
 * @author s
 */
public class DfsParsing implements Parsing {

    /**
     * 递归下降算法
     *
     * @return 语法树
     */
    private Boolean dfs(String code, int codeIndex, SyntaxTree syntaxTree, Stack<SyntaxTree.Node> productionMatch, int dep) {
        if (productionMatch.empty()) {
            return codeIndex == code.length();
        }

        SyntaxTree.Node currentNode = productionMatch.pop();
        String productionRaw = currentNode.getRaw();

        // 如果是终结符则匹配
        if (productionRaw.length() <= 1) {
            if (productionRaw.isEmpty()) {
                if (dfs(code, codeIndex, syntaxTree, productionMatch, dep + 1)) {
                    return true;
                }
            } else if (codeIndex < code.length() && code.charAt(codeIndex) == productionRaw.charAt(0)) {
                if (dfs(code, codeIndex + 1, syntaxTree, productionMatch, dep + 1)) {
                    return true;
                }
            }
        } else { // 非终结符则展开
            Grammar grammar = syntaxTree.getGrammar();
            for (Production production : grammar.getProductionsTable().get(productionRaw)) {
                List<SyntaxTree.Node> nodes = production.getDerive().stream()
                        .map(raw -> {
                            SyntaxTree.Node node = new SyntaxTree.Node();
                            node.setRaw(raw);
                            return node;
                        })
                        .collect(Collectors.toList());

                for (int i = nodes.size() - 1; i >= 0; i--) {
                    productionMatch.push(nodes.get(i));
                }
                currentNode.setProduction(production);
                currentNode.setSon(nodes);
                if (dfs(code, codeIndex, syntaxTree, productionMatch, dep + 1)) {
                    return true;
                }
                for (int i = production.getDerive().size() - 1; i >= 0; i--) {
                    productionMatch.pop();
                }
            }
        }
        productionMatch.push(currentNode);
        return false;

    }

    @Override
    public SyntaxTree parsing(String code, Grammar grammar) {
        SyntaxTree st = new SyntaxTree();
        st.setGrammar(grammar);

        SyntaxTree.Node node = new SyntaxTree.Node();
        node.setRaw(grammar.getTarget());
        st.setRoot(node);

        Stack<SyntaxTree.Node> stack = new Stack<>();
        stack.push(node);

        if (!dfs(code, 0, st, stack, 0)) {
            throw new RuntimeException("parsing failed");
        }
        return st;
    }

}
