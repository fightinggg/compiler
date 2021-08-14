package com.example.sdt;

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
                                    Map<String, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig) {
        res.put(node, new HashMap<>());
        List<SyntaxTree.Node> sonList = node.getSon();
        if (sonList == null) {
            // leaf
            res.get(node).put("tokenRaw", node.getToken().raw());
        } else {
            // innerNode
            sonList.forEach(son -> translation(son, res, innerNodeConfig));
            BiConsumer<Map<String, Object>, List<Map<String, Object>>> innerNodeConsumer = innerNodeConfig.get(node.getProduction().raw());
            if (innerNodeConsumer == null) {
                throw new RuntimeException("production [%s] could not found sdt function".formatted(node.getProduction()));
            }
            innerNodeConsumer.accept(res.get(node), sonList.stream().map(res::get).collect(Collectors.toList()));
        }
    }

    public static Map<String, Object> translation(
            SyntaxTree syntaxTree,
            Map<String, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig) {
        Map<SyntaxTree.Node, Map<String, Object>> res = new HashMap<>();
        translation(syntaxTree.getRoot(), res, innerNodeConfig);
        return res.get(syntaxTree.getRoot());
    }
}
