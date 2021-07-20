package parsing;

import grammar.NormalGrammarConfig;
import syntaxtree.SyntaxTree;

/**
 * 语法分析器
 *
 * @author s
 */
public interface Parsing {
    /**
     * 从源代码解析语法树
     *
     * @param code
     * @param normalGrammar
     * @return
     */
    SyntaxTree parsing(String code, NormalGrammarConfig normalGrammar);
}
