package lexical;

import grammar.NormalGrammarConfig;

import java.util.List;

/**
 * 词法分析器
 *
 * @author s
 */
public interface LexicalAnalysis {
    /**
     * 从源代码解析词法流
     *
     * @param code
     * @param normalGrammar
     * @return
     */
    List<Token> parsing(String code, NormalGrammarConfig normalGrammar);
}