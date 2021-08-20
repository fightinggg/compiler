package com.example.sdt;

import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;
import com.example.utils.ReadableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * S型语法制导翻译
 */
public class SyntaxDirectedTranslation {

    @FunctionalInterface
    public interface SyntaxDirectedTranslationConsumer {
        void apply(Map<String, Object> rt, List<Map<String, Object>> son, Runnable accessAllSon);
    }

    private static void translation(SyntaxTree.Node node,
                                    Map<SyntaxTree.Node, Map<String, Object>> res,
                                    Map<String, SyntaxDirectedTranslationConsumer> innerNodeConfig) {
        res.putIfAbsent(node, new HashMap<>());
        List<SyntaxTree.Node> sonNodeList = node.getSon();
        if (sonNodeList == null) {
            // leaf
            res.get(node).put("tokenRaw", node.getToken().raw());
        } else {
            // innerNode

            SyntaxDirectedTranslationConsumer innerNodeConsumer = innerNodeConfig.get(node.getProduction().raw());
            if (innerNodeConsumer == null) {
                throw new RuntimeException("production [%s] could not found sdt function".formatted(node.getProduction()));
            }

            ArrayList<Map<String, Object>> innerList = new ArrayList<>();
            List<Map<String, Object>> sonList = new ReadableList<>(innerList);
            innerNodeConsumer.apply(res.get(node),
                    sonList,
                    () -> {
                        sonNodeList.forEach(son -> translation(son, res, innerNodeConfig));
                        innerList.addAll(sonNodeList.stream().map(res::get).collect(Collectors.toList()));
                    });
        }
    }

    public static Map<SyntaxTree.Node, Map<String, Object>> translation(
            Map<SyntaxTree.Node, Map<String, Object>> res,
            SyntaxTree syntaxTree,
            Map<String, SyntaxDirectedTranslationConsumer> innerNodeConfig) {
        translation(syntaxTree.getRoot(), res, innerNodeConfig);
        return res;//.get(syntaxTree.getRoot());
    }
}
