package com.example.sdt;

import com.example.grammar.Production;
import com.example.syntaxtree.SyntaxTree;

import java.util.HashMap;
import java.util.Map;

/**
 * S型语法制导翻译
 */
public class SyntaxDirectedTranslation {
    private void translation(SyntaxTree.Node node, Map<SyntaxTree.Node, Map<String, Object>> res) {
        if (node.getSon() == null) {

        }
    }

    public Map<SyntaxTree.Node, Map<String, Object>> translation(SyntaxTree syntaxTree, Map<Production,>) {
        Map<SyntaxTree.Node, Map<String, Object>> res = new HashMap<>();
        translation(syntaxTree.getRoot(), res);
        return res;
    }
}
