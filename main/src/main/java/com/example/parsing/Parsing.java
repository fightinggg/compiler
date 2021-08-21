package com.example.parsing;

import com.example.grammar.GrammarConfigImpl;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;

import java.util.List;

/**
 * 语法分析器
 *
 * @author s
 */
public interface Parsing {
    /**
     * 从源代码解析语法树
     *
     * @param tokens
     * @param normalGrammar
     * @return
     */
    SyntaxTree parsing(List<Token> tokens, GrammarConfigImpl normalGrammar);
}
