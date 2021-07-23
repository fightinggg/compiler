package com.example.syntaxtree;

import com.example.grammar.GrammarConfig;
import com.example.grammar.Production;
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
        private String raw;

        private Production production;
        private List<Node> son;
    }

    private GrammarConfig grammarConfig;
    private Node root;
}
