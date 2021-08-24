package com.example.lang.json;

import com.example.sdt.SyntaxDirectedTranslation;
import com.example.sdt.SyntaxDirectedTranslationUtils;
import com.example.syntaxtree.SyntaxTree;
import com.example.utils.MergeableCollection;
import com.example.utils.MergeableMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonSyntaxDirectedTranslation implements SyntaxDirectedTranslation<Object> {
    Map<String, Object> toMap(Object o) {
        return (Map<String, Object>) o;
    }

    Collection<Object> toCollection(Object o) {
        return (Collection<Object>) o;
    }

    @Override
    public Object translation(SyntaxTree syntaxTree) {
        Map<String, SyntaxDirectedTranslationUtils.SyntaxDirectedTranslationConsumer> innerNodeConfig = Map.ofEntries(
                Map.entry("object -> string", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", son.get(0).get("tokenRaw"));
                }),
                Map.entry("object -> number", (fa, rt, son, accessAllSon) -> {
                    throw new RuntimeException();
                }),
                Map.entry("object -> array", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", son.get(0).get("object"));
                }),
                Map.entry("object -> leftCurlyBracket kvs rightCurlyBracket", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", son.get(1).get("object"));
                }),
                Map.entry("object -> leftCurlyBracket rightCurlyBracket", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", Map.of());
                }),
                Map.entry("target -> object", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", son.get(0).get("object"));
                }),
                Map.entry("array -> leftSquareBracket objects rightSquareBracket", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", son.get(1).get("object"));
                }),
                Map.entry("array -> leftSquareBracket rightSquareBracket", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", List.of());
                }),
                Map.entry("kvs -> kv", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", son.get(0).get("object"));
                }),
                Map.entry("kvs -> kvs comma kv", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    Map<String, Object> kvs = toMap(son.get(0).get("object"));
                    Map<String, Object> kv = toMap(son.get(2).get("object"));
                    rt.put("object", MergeableMap.merge(kvs, kv));
                }),
                Map.entry("kv -> string colon object", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    String key = toString(son.get(0).get("tokenRaw"));
                    Object value = son.get(2).get("object");
                    rt.put("object", Map.of(key, value));
                }),
                Map.entry("objects -> object", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("object", List.of(son.get(0).get("object")));
                }),
                Map.entry("objects -> objects comma object", (fa, rt, son, accessAllSon) -> {
                    accessAllSon.run();
                    Collection<Object> l1 = toList(son.get(0).get("object"));
                    Collection<Object> l2 = List.of(son.get(2).get("object"));
                    rt.put("object", MergeableCollection.merge(l1, l2));
                })
        );
        Map<SyntaxTree.Node, Map<String, Object>> translation = SyntaxDirectedTranslationUtils.translation(new HashMap<>(), syntaxTree, innerNodeConfig);
        return translation.get(syntaxTree.getRoot()).get("object");
    }

    private Collection<Object> toList(Object object) {
        return (Collection<Object>) object;
    }

    private String toString(Object tokenRaw) {
        return (String) tokenRaw;
    }
}
