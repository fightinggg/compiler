package com.example.syntaxtree;

import com.example.grammar.GrammarConfig;
import com.example.grammar.Production;
import com.example.lexical.Token;
import lombok.AllArgsConstructor;
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
    @AllArgsConstructor
    public static class Node {
        // 叶子节点用这个值
        private Token token;
        // 内部节点用这个值
        private Production production;
        // 子节点
        private List<Node> son;
    }

    private GrammarConfig grammarConfig;
    private Node root;
}
