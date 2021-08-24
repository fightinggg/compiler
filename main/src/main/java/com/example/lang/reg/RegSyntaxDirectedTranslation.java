package com.example.lang.reg;

import com.example.nfa.Nfa;
import com.example.nfa.NfaUtils;
import com.example.sdt.SyntaxDirectedTranslation;
import com.example.sdt.SyntaxDirectedTranslationUtils;
import com.example.syntaxtree.SyntaxTree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegSyntaxDirectedTranslation implements SyntaxDirectedTranslation<Nfa<Object, String>> {
    public static List<String> toListString(Object o) {
        return (List<String>) o;
    }

    public static String toString(Object o) {
        return (String) o;
    }


    public static Nfa<Object, String> toNfa(Object o) {
        return (Nfa<Object, String>) o;
    }

    @Override
    public Nfa<Object, String> translation(SyntaxTree syntaxTree) {
        final Map<String, SyntaxDirectedTranslationUtils.SyntaxDirectedTranslationConsumer> innerNodeConfig = Map.ofEntries(
                Map.entry("numberSeq -> number sub number", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    int from = Integer.parseInt((toString(sonList.get(0).get("tokenRaw"))));
                    int to = Integer.parseInt(toString(sonList.get(2).get("tokenRaw")));
                    List<Nfa<Object, String>> nfaList = IntStream.range(from, to + 1)
                            .mapToObj(String::valueOf)
                            .map(NfaUtils::oneChar)
                            .collect(Collectors.toList());
                    Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, Nfa.EMPTY_TRANS);
                    rt.put("nfa", parallel);
                }),
                Map.entry("lowerCaseLetterSeq -> lowerCaseLetter sub lowerCaseLetter", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String from = (String) sonList.get(0).get("tokenRaw");
                    String to = (String) sonList.get(2).get("tokenRaw");
                    List<Nfa<Object, String>> nfaList = IntStream.range(from.charAt(0) - 'a', to.charAt(0) - 'a' + 1)
                            .mapToObj(c -> (char) (c + 'a'))
                            .map(String::valueOf)
                            .map(NfaUtils::oneChar)
                            .collect(Collectors.toList());
                    Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, Nfa.EMPTY_TRANS);
                    rt.put("nfa", parallel);
                }),
                Map.entry("lowerCaseLetterSeq -> upperCaseLetter sub upperCaseLetter", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String from = (String) sonList.get(0).get("tokenRaw");
                    String to = (String) sonList.get(2).get("tokenRaw");
                    List<Nfa<Object, String>> nfaList = IntStream.range(from.charAt(0) - 'A', to.charAt(0) - 'A' + 1)
                            .mapToObj(c -> (char) (c + 'A'))
                            .map(String::valueOf)
                            .map(NfaUtils::oneChar)
                            .collect(Collectors.toList());
                    Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, Nfa.EMPTY_TRANS);
                    rt.put("nfa", parallel);
                }),
                Map.entry("letterSeq -> lowerCaseLetterSeq", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry("letterSeq -> upperCaseLetterSeq", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry("char -> number", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("tokenRaw")));
                    rt.put("valueList", List.of(sonList.get(0).get("tokenRaw")));
                }),
                Map.entry("char -> lowerCaseLetter", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("tokenRaw")));
                    rt.put("valueList", List.of(sonList.get(0).get("tokenRaw")));
                }),
                Map.entry("char -> upperCaseLetter", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("tokenRaw")));
                    rt.put("valueList", List.of(sonList.get(0).get("tokenRaw")));
                }),
                Map.entry("char -> escape", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("tokenRaw")));
                    rt.put("valueList", List.of(sonList.get(0).get("tokenRaw")));
                }),
                Map.entry("char -> blankSet", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    final List<String> values = Arrays.stream(" \n\t".split("")).toList();
                    Nfa<Object, String> nfa = NfaUtils.someCharParallel(values);
                    rt.put("nfa", nfa);
                    rt.put("valueList", values);
                }),
                Map.entry("char -> simpleNumberSet", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    final List<String> values = Arrays.stream("0123456789".split("")).toList();
                    Nfa<Object, String> nfa = NfaUtils.someCharParallel(values);
                    rt.put("nfa", nfa);
                    rt.put("valueList", values);
                }),
                Map.entry("char -> letterSet", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    final List<String> values = Arrays.stream("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split("")).toList();
                    Nfa<Object, String> nfa = NfaUtils.someCharParallel(values);
                    rt.put("nfa", nfa);
                    rt.put("valueList", values);
                }),
                Map.entry("char -> specialChar", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", NfaUtils.oneChar(sonList.get(0).get("tokenRaw")));
                    rt.put("valueList", List.of(sonList.get(0).get("tokenRaw")));
                }),
                Map.entry("atLeastOneChar -> char", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("valueList", sonList.get(0).get("valueList"));
                }),
                Map.entry("atLeastOneChar -> atLeastOneChar char", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    List<String> values = toListString(sonList.get(0).get("valueList"));
                    values = new ArrayList<>(values);
                    values.addAll(toListString(sonList.get(1).get("valueList")));
                    rt.put("valueList", values);

                }),
                Map.entry("unit -> char", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry("unit -> unit star", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Nfa<Object, String> nfa = toNfa(sonList.get(0).get("nfa"));
                    rt.put("nfa", NfaUtils.parallel(NfaUtils.selfLoop(nfa, Nfa.EMPTY_TRANS), NfaUtils.oneChar(Nfa.EMPTY_TRANS), Nfa.EMPTY_TRANS));
                }),
                Map.entry("unit -> unit add", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Nfa<Object, String> nfa = toNfa(sonList.get(0).get("nfa"));
                    rt.put("nfa", NfaUtils.series(nfa, NfaUtils.selfLoop(nfa, Nfa.EMPTY_TRANS), Nfa.EMPTY_TRANS));
                }),
                Map.entry("unit -> unit questionMark", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Nfa<Object, String> nfa = toNfa(sonList.get(0).get("nfa"));
                    rt.put("nfa", NfaUtils.parallel(nfa, NfaUtils.oneChar(Nfa.EMPTY_TRANS), Nfa.EMPTY_TRANS));
                }),
                Map.entry("unit -> unit leftCurlyBracket number rightCurlyBracket", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Nfa<Object, String> nfa = toNfa(sonList.get(0).get("nfa"));
                    List<Nfa<Object, String>> nfaList = IntStream.range(0, Integer.parseInt(toString(sonList.get(2).get("tokenRaw"))))
                            .mapToObj(i -> nfa)
                            .collect(Collectors.toList());
                    rt.put("nfa", NfaUtils.series(nfaList, Nfa.EMPTY_TRANS));
                }),
                Map.entry("unit -> leftSquareBracket atLeastOneChar rightSquareBracket", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    final List<String> values = toListString(sonList.get(1).get("valueList"));
                    rt.put("nfa", NfaUtils.someCharParallel(values));
                }),
                Map.entry("unit -> leftBracket unitSeq rightBracket", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(1).get("nfa"));
                }),
                Map.entry("atLeastOneUnit -> unit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry("atLeastOneUnit -> atLeastOneUnit unit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Nfa<Object, String> nfa1 = toNfa(sonList.get(0).get("nfa"));
                    Nfa<Object, String> nfa2 = toNfa(sonList.get(1).get("nfa"));
                    rt.put("nfa", NfaUtils.series(nfa1, nfa2, Nfa.EMPTY_TRANS));
                }),
                Map.entry("unitSeq -> atLeastOneUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(0).get("nfa"));
                }),
                Map.entry("unitSeq -> unitSeq or atLeastOneUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Nfa<Object, String> nfa1 = toNfa(sonList.get(0).get("nfa"));
                    Nfa<Object, String> nfa2 = toNfa(sonList.get(2).get("nfa"));
                    rt.put("nfa", NfaUtils.parallel(nfa1, nfa2, Nfa.EMPTY_TRANS));
                }),
                Map.entry("target -> unitSeq", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("nfa", sonList.get(0).get("nfa"));
                })
        );
        Map<SyntaxTree.Node, Map<String, Object>> translation = SyntaxDirectedTranslationUtils.translation(new HashMap<>(), syntaxTree, innerNodeConfig);
        return toNfa(translation.get(syntaxTree.getRoot()).get("nfa"));
    }
}
