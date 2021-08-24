package com.example.sdt;

import com.example.syntaxtree.SyntaxTree;
import com.example.utils.ParrentableMap;
import com.example.utils.RoList;
import com.example.utils.RoMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * S型语法制导翻译
 */
public class SyntaxDirectedTranslationUtils {

    @FunctionalInterface
    public interface SyntaxDirectedTranslationConsumer {
        void apply(RoMap<String, Object> fa, Map<String, Object> rt, List<Map<String, Object>> son, Runnable accessAllSon);
    }

    private static void translation(
            RoMap<String, Object> context,
            SyntaxTree.Node node,
            Map<SyntaxTree.Node, Map<String, Object>> allNodeContext,
            Map<String, SyntaxDirectedTranslationConsumer> innerNodeConfig) {
        allNodeContext.putIfAbsent(node, new HashMap<>());
        List<SyntaxTree.Node> sonNodeList = node.getSon();
        if (sonNodeList == null) {
            // leaf
            allNodeContext.get(node).put("tokenRaw", node.getToken().raw());
        } else {
            // innerNode

            SyntaxDirectedTranslationConsumer innerNodeConsumer = innerNodeConfig.get(node.getProduction().raw());
            if (innerNodeConsumer == null) {
                throw new RuntimeException("production [%s] could not found sdt function".formatted(node.getProduction()));
            }

            ArrayList<Map<String, Object>> innerList = new ArrayList<>();
            List<Map<String, Object>> sonList = new RoList<>(innerList);
            innerNodeConsumer.apply(
                    context,
                    allNodeContext.get(node),
                    sonList,
                    () -> {
                        sonNodeList.forEach(son -> translation(new RoMap<>(new ParrentableMap<>(allNodeContext.get(node), context)), son, allNodeContext, innerNodeConfig));
                        innerList.addAll(sonNodeList.stream().map(allNodeContext::get).collect(Collectors.toList()));
                    });
        }
    }

    public static Map<SyntaxTree.Node, Map<String, Object>> translation(
            Map<SyntaxTree.Node, Map<String, Object>> res,
            SyntaxTree syntaxTree,
            Map<String, SyntaxDirectedTranslationConsumer> innerNodeConfig) {
        translation(new RoMap<>(new HashMap<>()), syntaxTree.getRoot(), res, innerNodeConfig);
        return res;//.get(syntaxTree.getRoot());
    }
}
