package com.example.sdt;

import com.example.grammar.Production;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * S型语法制导翻译
 */
public class SyntaxDirectedTranslation {
    private static void translation(SyntaxTree.Node node,
                                    Map<SyntaxTree.Node, Map<String, Object>> res,
                                    Map<Production, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig,
                                    Map<String, BiConsumer<Map<String, Object>, Token>> leafConfig) {
        res.put(node, new HashMap<>());
        List<SyntaxTree.Node> sonList = node.getSon();
        if (sonList == null) {
            // leaf
            leafConfig.get(node.getToken().type()).accept(res.get(node), node.getToken());
        } else {
            // innerNode
            sonList.forEach(son -> translation(son, res, innerNodeConfig, leafConfig));
            BiConsumer<Map<String, Object>, List<Map<String, Object>>> innerNodeConsumer = innerNodeConfig.get(node.getProduction());
            if (innerNodeConsumer == null) {
                throw new RuntimeException("production [%s] could not found sdt function".formatted(node.getProduction()));
            }
            innerNodeConsumer.accept(res.get(node), sonList.stream().map(res::get).collect(Collectors.toList()));
        }
    }

    public static void translation(
            SyntaxTree syntaxTree,
            Map<Production, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig,
            Map<String, BiConsumer<Map<String, Object>, Token>> leafConfig) {
        Map<SyntaxTree.Node, Map<String, Object>> res = new HashMap<>();
        translation(syntaxTree.getRoot(), res, innerNodeConfig, leafConfig);
    }
}
