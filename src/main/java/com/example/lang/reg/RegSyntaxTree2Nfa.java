package com.example.lang.reg;

import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import com.example.lexical.Token;
import com.example.lexical.TokenImpl;
import com.example.nfa.Nfa;
import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RegSyntaxTree2Nfa {
    public static Nfa<Object, Token> toNfa(SyntaxTree syntaxTree) {
        final Map<Production, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig = Map.ofEntries(
                Map.entry(new ProductionImpl("numberSeq -> number sub number"), (rt, sonList) -> {

                })
//                Map.entry(new ProductionImpl("1", new ArrayList<>()), null),
//                Map.entry(new ProductionImpl("1", new ArrayList<>()), null),
//                Map.entry(new ProductionImpl("1", new ArrayList<>()), null),
//                Map.entry(new ProductionImpl("1", new ArrayList<>()), null),
//                Map.entry(new ProductionImpl("1", new ArrayList<>()), null)
        );
        final Map<String, BiConsumer<Map<String, Object>, Token>> leafConfig = Map.ofEntries(
                Map.entry("number", (mp, token) -> mp.put("value", Integer.valueOf(token.raw()))),
                Map.entry("lowerCaseLetter", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("upperCaseLetter", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("escape", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("specialChar", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("leftBracket", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("or", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("leftSquareBracket", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("rightSquareBracket", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("leftCurlyBracket", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("rightCurlyBracket", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("star", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("add", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("questionMark", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("sub", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("dot", (mp, token) -> mp.put("value", token.raw()))
        );

        SyntaxDirectedTranslation.translation(syntaxTree, innerNodeConfig, leafConfig);
        return null;
    }
}
