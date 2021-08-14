package com.example.lang.cpp;

import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;
import com.example.utils.MergeableCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.BiConsumer;

public class CppSyntaxDirectedTranslation {

    @Getter
    @AllArgsConstructor
    public static class ThreeAddressCode {
        public static final String MUL = "mul";
        public static final String DIV = "div";
        public static final String MOD = "mod";
        public static final String ASSIGN = "assign";
        public static final String ASSIGN_NUMBER = "assignNumber";
        String operator;
        String target;
        String op1;
        String op2;

        @Override
        public String toString() {
            return "%-20s %-3s %-3s %-4s".formatted(operator, target, op1, op2);
        }
    }

    public static List<String> toListString(Object o) {
        return (List<String>) o;
    }

    public static String toString(Object o) {
        return (String) o;
    }

    public static MergeableCollection<ThreeAddressCode> translation(SyntaxTree syntaxTree) {
        final int[] tmpId = {0};
        String varPrefix = "v";
        final Map<String, BiConsumer<Map<String, Object>, List<Map<String, Object>>>> innerNodeConfig = Map.ofEntries(
                Map.entry("type -> symbol", (rt, sonList) -> {
                    rt.put("typeName", sonList.get(0).get("tokenRaw"));
                }),
                Map.entry("leftSymbol -> symbol", (rt, sonList) -> {
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                }),
                Map.entry("someRightSymbolDefineJoinByCommaNotEmpty -> type rightSymbol", (rt, sonList) -> {
                    rt.put("typeList", List.of(sonList.get(0).get("typeName")));
                    rt.put("addressList", List.of(sonList.get(1).get("address")));
                }),
                Map.entry("someRightSymbolDefineJoinByCommaNotEmpty -> someRightSymbolDefineJoinByCommaNotEmpty comma type rightSymbol", (rt, sonList) -> {
                    List<String> typeList = new ArrayList<>(toListString(sonList.get(0).get("typeList")));
                    List<String> addressList = new ArrayList<>(toListString(sonList.get(0).get("addressList")));
                    typeList.add(toString(sonList.get(2).get("typeName")));
                    addressList.add(toString(sonList.get(3).get("address")));
                    rt.put("typeList", typeList);
                    rt.put("addressList", addressList);
                }),
                Map.entry("functionStatement -> type symbol leftBracket rightBracket leftCurlyBracket block rightCurlyBracket", (rt, sonList) -> {
                    rt.putAll(sonList.get(5));
                }),
                Map.entry("functionStatement -> type symbol leftBracket someRightSymbolDefineJoinByCommaNotEmpty rightBracket leftCurlyBracket block rightCurlyBracket", (rt, sonList) -> {
                    rt.putAll(sonList.get(6));
                }),
                Map.entry("rightSymbol -> symbol", (rt, sonList) -> {
                    rt.put("codes", new ArrayList<>());
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                }),
                Map.entry("rightSymbol -> number", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<ThreeAddressCode> currentCode = List.of(new ThreeAddressCode(ThreeAddressCode.ASSIGN_NUMBER, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                }),
                Map.entry("rightSymbol -> rightSymbol mul rightSymbol", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String ad1 = toString(sonList.get(0).get("address"));
                    String ad2 = toString(sonList.get(2).get("address"));
                    List<ThreeAddressCode> currentCode = List.of(new ThreeAddressCode(ThreeAddressCode.MUL, address, ad1, ad2));

                    Collection<ThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<ThreeAddressCode> codes2 = toCodes(sonList.get(2).get("codes"));
                    MergeableCollection<ThreeAddressCode> codes = new MergeableCollection<>(codes1, codes2, currentCode);

                    rt.put("codes", codes);
                    rt.put("address", address);
                }),
                Map.entry("rightSymbol -> rightSymbol div rightSymbol", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String ad1 = toString(sonList.get(0).get("address"));
                    String ad2 = toString(sonList.get(2).get("address"));
                    List<ThreeAddressCode> currentCode = List.of(new ThreeAddressCode(ThreeAddressCode.DIV, address, ad1, ad2));

                    Collection<ThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<ThreeAddressCode> codes2 = toCodes(sonList.get(2).get("codes"));
                    MergeableCollection<ThreeAddressCode> codes = new MergeableCollection<>(codes1, codes2, currentCode);

                    rt.put("codes", codes);
                    rt.put("address", address);
                }),
                Map.entry("rightSymbol -> rightSymbol mod rightSymbol", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String ad1 = toString(sonList.get(0).get("address"));
                    String ad2 = toString(sonList.get(2).get("address"));
                    List<ThreeAddressCode> currentCode = List.of(new ThreeAddressCode(ThreeAddressCode.MOD, address, ad1, ad2));

                    Collection<ThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<ThreeAddressCode> codes2 = toCodes(sonList.get(2).get("codes"));
                    MergeableCollection<ThreeAddressCode> codes = new MergeableCollection<>(codes1, codes2, currentCode);

                    rt.put("codes", codes);
                    rt.put("address", address);
                }),
                Map.entry("symbolStatementSentence -> type leftSymbol eq rightSymbol", (rt, sonList) -> {
                    String address = toString(sonList.get(1).get("address"));
                    String ad = toString(sonList.get(3).get("address"));
                    List<ThreeAddressCode> currentCode = List.of(new ThreeAddressCode(ThreeAddressCode.ASSIGN, address, ad, null));

                    Collection<ThreeAddressCode> codes1 = toCodes(sonList.get(3).get("codes"));
                    MergeableCollection<ThreeAddressCode> codes = new MergeableCollection<>(codes1, currentCode);

                    rt.put("codes", codes);
                    rt.put("address", address);
                }),
                Map.entry("sentence -> symbolStatementSentence", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("blockUnit -> sentence semicolon", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("block -> blockUnit", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("cFileUnit -> functionStatement", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("someCFileUnit -> cFileUnit", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                })
        );
        return (MergeableCollection<ThreeAddressCode>) SyntaxDirectedTranslation.translation(syntaxTree, innerNodeConfig).get("codes");
    }

    private static Collection<ThreeAddressCode> toCodes(Object codes) {
        return (Collection<ThreeAddressCode>) codes;
    }
}
