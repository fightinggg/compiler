package com.example.lang.reg;

import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import com.example.lexical.Token;
import com.example.lexical.TokenImpl;
import com.example.nfa.Nfa;
import com.example.nfa.NfaUtils;
import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegSyntaxDirectedTranslation {
    public static final String EMPTY = "$empty";


    public static Nfa<Object, String> toNfa(SyntaxTree syntaxTree) {
        final Map<Production, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig = Map.ofEntries(
                Map.entry(new ProductionImpl("numberSeq -> number sub number"), (rt, sonList) -> {
                    Integer from = Integer.valueOf((String) sonList.get(0).get("value"));
                    Integer to = Integer.valueOf((String) sonList.get(2).get("value"));
                    List<Nfa<Object, String>> nfaList = IntStream.range(from, to + 1)
                            .mapToObj(String::valueOf)
                            .map(NfaUtils::oneChar)
                            .collect(Collectors.toList());
                    Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, EMPTY);
                    rt.put("nfa", parallel);
                }),
                Map.entry(new ProductionImpl("lowerCaseLetterSeq -> lowerCaseLetter sub lowerCaseLetter"), (rt, sonList) -> {
                    String from = (String) sonList.get(0).get("value");
                    String to = (String) sonList.get(2).get("value");
                    List<Nfa<Object, String>> nfaList = IntStream.range(from.charAt(0) - 'a', to.charAt(0) - 'a' + 1)
                            .mapToObj(c -> (char) (c + 'a'))
                            .map(String::valueOf)
                            .map(NfaUtils::oneChar)
                            .collect(Collectors.toList());
                    Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, EMPTY);
                    rt.put("nfa", parallel);
                }),
                Map.entry(new ProductionImpl("lowerCaseLetterSeq -> upperCaseLetter sub upperCaseLetter"), (rt, sonList) -> {
                    String from = (String) sonList.get(0).get("value");
                    String to = (String) sonList.get(2).get("value");
                    List<Nfa<Object, String>> nfaList = IntStream.range(from.charAt(0) - 'A', to.charAt(0) - 'A' + 1)
                            .mapToObj(c -> (char) (c + 'A'))
                            .map(String::valueOf)
                            .map(NfaUtils::oneChar)
                            .collect(Collectors.toList());
                    Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, EMPTY);
                    rt.put("nfa", parallel);
                }),
                Map.entry(new ProductionImpl("letterSeq -> lowerCaseLetterSeq"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry(new ProductionImpl("letterSeq -> upperCaseLetterSeq"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry(new ProductionImpl("char -> number"), (rt, sonList) -> {
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("value")));
                }),
                Map.entry(new ProductionImpl("char -> lowerCaseLetter"), (rt, sonList) -> {
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("value")));
                }),
                Map.entry(new ProductionImpl("char -> upperCaseLetter"), (rt, sonList) -> {
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("value")));
                }),
                Map.entry(new ProductionImpl("char -> escape"), (rt, sonList) -> {
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("value")));
                }),
                Map.entry(new ProductionImpl("char -> specialChar"), (rt, sonList) -> {
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("value")));
                }),
                Map.entry(new ProductionImpl("someChar -> "), (rt, sonList) -> {
                    rt.put("someCharNfaList", List.of(NfaUtils.oneChar(EMPTY)));
                }),
                Map.entry(new ProductionImpl("someChar -> someChar char"), (rt, sonList) -> {
                    List<Nfa<Object, String>> someCharNfaList = new ArrayList<>((List<Nfa<Object, String>>) sonList.get(0).get("someCharNfaList"));
                    someCharNfaList.add((Nfa<Object, String>) sonList.get(1).get("nfa"));
                    rt.put("someCharNfaList", someCharNfaList);
                }),
                Map.entry(new ProductionImpl("unit -> char"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry(new ProductionImpl("unit -> unit star"), (rt, sonList) -> {
                    Nfa<Object, String> nfa = (Nfa<Object, String>) sonList.get(0).get("nfa");
                    rt.put("nfa", NfaUtils.selfLoop(nfa, EMPTY));
                }),
                Map.entry(new ProductionImpl("unit -> unit add"), (rt, sonList) -> {
                    Nfa<Object, String> nfa = (Nfa<Object, String>) sonList.get(0).get("nfa");
                    rt.put("nfa", NfaUtils.series(nfa, NfaUtils.selfLoop(nfa, EMPTY), EMPTY));
                }),
                Map.entry(new ProductionImpl("unit -> unit questionMark"), (rt, sonList) -> {
                    Nfa<Object, String> nfa = (Nfa<Object, String>) sonList.get(0).get("nfa");
                    rt.put("nfa", NfaUtils.parallel(nfa, NfaUtils.oneChar(EMPTY), EMPTY));
                }),
                Map.entry(new ProductionImpl("unit -> unit leftCurlyBracket number rightCurlyBracket"), (rt, sonList) -> {
                    Nfa<Object, String> nfa = (Nfa<Object, String>) sonList.get(0).get("nfa");
                    List<Nfa<Object, String>> nfaList = IntStream.range(0, Integer.valueOf((String) sonList.get(2).get("value")))
                            .mapToObj(i -> nfa)
                            .collect(Collectors.toList());
                    rt.put("nfa", NfaUtils.series(nfaList, EMPTY));
                }),
                Map.entry(new ProductionImpl("unit -> leftSquareBracket someChar rightSquareBracket"), (rt, sonList) -> {
                    List<Nfa<Object, String>> someCharNfaList = (List<Nfa<Object, String>>) sonList.get(1).get("someCharNfaList");
                    rt.put("nfa", NfaUtils.parallel(someCharNfaList, EMPTY));
                }),
                Map.entry(new ProductionImpl("unit -> leftBracket unitSeq rightBracket"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(1).get("nfa"));
                }),
                Map.entry(new ProductionImpl("atLeastOneUnit -> unit"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry(new ProductionImpl("atLeastOneUnit -> atLeastOneUnit unit"), (rt, sonList) -> {
                    Nfa<Object, String> nfa1 = (Nfa<Object, String>) sonList.get(0).get("nfa");
                    Nfa<Object, String> nfa2 = (Nfa<Object, String>) sonList.get(1).get("nfa");
                    rt.put("nfa", NfaUtils.series(nfa1, nfa2, EMPTY));
                }),
                Map.entry(new ProductionImpl("unitSeq -> atLeastOneUnit"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry(new ProductionImpl("unitSeq -> unitSeq or atLeastOneUnit"), (rt, sonList) -> {
                    Nfa<Object, String> nfa1 = (Nfa<Object, String>) sonList.get(0).get("nfa");
                    Nfa<Object, String> nfa2 = (Nfa<Object, String>) sonList.get(2).get("nfa");
                    rt.put("nfa", NfaUtils.parallel(nfa1, nfa2, EMPTY));
                }),
                Map.entry(new ProductionImpl("target -> unitSeq"), (rt, sonList) -> {
                    rt.put("nfa", sonList.get(0).get("nfa"));
                })
        );
        final Map<String, BiConsumer<Map<String, Object>, Token>> leafConfig = Map.ofEntries(
                Map.entry("number", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("lowerCaseLetter", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("upperCaseLetter", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("escape", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("specialChar", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("leftBracket", (mp, token) -> mp.put("value", token.raw())),
                Map.entry("rightBracket", (mp, token) -> mp.put("value", token.raw())),
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

        return SyntaxDirectedTranslation.translation(syntaxTree, innerNodeConfig, leafConfig);
    }
}
