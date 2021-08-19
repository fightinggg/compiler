package com.example.lang.cpp;

import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;
import com.example.utils.MergeableCollection;
import com.example.pava.impl.PavaDefaultThreeAddressCode;

import java.util.*;
import java.util.function.BiConsumer;

public class CppSyntaxDirectedTranslation {


    private static Collection<String> toStringCollection(Object o) {
        return (Collection<String>) o;
    }

    private static String toString(Object o) {
        return (String) o;
    }

    private static Collection<PavaDefaultThreeAddressCode> toCodes(Object codes) {
        return (Collection<PavaDefaultThreeAddressCode>) codes;
    }


    private static void binaryOperator(String varPrefix, int[] tmpId, Map<String, Object> rt, List<Map<String, Object>> sonList, String op) {
        String address = varPrefix + tmpId[0]++;
        String ad1 = toString(sonList.get(0).get("address"));
        String ad2 = toString(sonList.get(2).get("address"));
        Collection<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(op, address, ad1, ad2));

        Collection<PavaDefaultThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
        Collection<PavaDefaultThreeAddressCode> codes2 = toCodes(sonList.get(2).get("codes"));
        Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(codes1, codes2, currentCode);

        Collection<String> scope1 = toStringCollection(sonList.get(0).get("scope"));
        Collection<String> scope2 = toStringCollection(sonList.get(2).get("scope"));
        Collection<String> scope = MergeableCollection.merge(List.of(address), scope1, scope2);

        rt.put("codes", codes);
        rt.put("address", address);
        rt.put("scope", scope);
    }

    public static Collection<PavaDefaultThreeAddressCode> translation(SyntaxTree syntaxTree) {
        final int[] tmpId = {0};
        String varPrefix = "symbol_";
        String labelPrefix = "label_";
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
                    List<String> typeList = new ArrayList<>(toStringCollection(sonList.get(0).get("typeList")));
                    List<String> addressList = new ArrayList<>(toStringCollection(sonList.get(0).get("addressList")));
                    typeList.add(toString(sonList.get(2).get("typeName")));
                    addressList.add(toString(sonList.get(3).get("address")));
                    rt.put("typeList", typeList);
                    rt.put("addressList", addressList);
                }),
                Map.entry("functionStatement -> type symbol leftBracket rightBracket leftCurlyBracket block rightCurlyBracket", (rt, sonList) -> {
                    String funcionName = toString(sonList.get(1).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> functionLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, funcionName, null, null));
//
//                    Collection<ThreeAddressCode> parmaCodes = toListString(sonList.get(3).get("addressList")).stream()
//                            .map(ad -> new ThreeAddressCode(ThreeAddressCode.PARMA_LOAD, ad, null, null))
//                            .toList();

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(5).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(functionLabelCodes, /*parmaCodes,*/ blockCodes);

                    rt.put("codes", codes);
                }),
                Map.entry("functionStatement -> type symbol leftBracket someRightSymbolDefineJoinByCommaNotEmpty rightBracket leftCurlyBracket block rightCurlyBracket", (rt, sonList) -> {
                    String funcionName = toString(sonList.get(1).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> functionLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, funcionName, null, null));

                    List<String> addressList = new ArrayList<>(toStringCollection(sonList.get(3).get("addressList")));
                    Collections.reverse(addressList);
                    Collection<PavaDefaultThreeAddressCode> parmaLoadCodes = addressList.stream()
                            .map(ad -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.PARMA_LOAD, ad, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(6).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(functionLabelCodes, parmaLoadCodes, blockCodes);

                    rt.put("codes", codes);
                }),
                Map.entry("someRightSymbolJoinByCommaNotEmpty -> rightSymbol", (rt, sonList) -> {
                    String rightSymbolAddress = toString(sonList.get(0).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(0).get("codes"));

                    rt.put("addressList", List.of(rightSymbolAddress));
                    rt.put("codes", rightSymbolCodes);
                }),
                Map.entry("someRightSymbolJoinByCommaNotEmpty -> someRightSymbolJoinByCommaNotEmpty comma rightSymbol", (rt, sonList) -> {
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));

                    Collection<String> oldAddress = toStringCollection(sonList.get(0).get("addressList"));
                    Collection<PavaDefaultThreeAddressCode> oldCodes = toCodes(sonList.get(0).get("codes"));

                    Collection<String> addressList = MergeableCollection.merge(oldAddress, List.of(rightSymbolAddress));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(oldCodes, rightSymbolCodes);

                    rt.put("addressList", addressList);
                    rt.put("codes", codes);
                }),
                Map.entry("functionInvoke -> symbol leftBracket rightBracket", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String functionName = toString(sonList.get(0).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> callCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.CALL, address, functionName, null));

                    rt.put("codes", callCodes);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                Map.entry("functionInvoke -> symbol leftBracket someRightSymbolJoinByCommaNotEmpty rightBracket", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String functionName = toString(sonList.get(0).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> parmaCodes = toStringCollection(sonList.get(2).get("addressList")).stream()
                            .map(ad -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.PARMA_PUT, ad, null, null))
                            .toList();
                    Collection<PavaDefaultThreeAddressCode> callCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.CALL, address, functionName, null));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(parmaCodes, callCodes);

                    rt.put("codes", codes);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                Map.entry("rightSymbol -> symbol", (rt, sonList) -> {
                    rt.put("codes", new ArrayList<>());
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                    rt.put("scope", List.of());
                }),
                Map.entry("rightSymbol -> number", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                Map.entry("rightSymbol -> functionInvoke", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("rightSymbol -> string", (rt, sonList) -> {
                    String address = varPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_STRING, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                Map.entry("rightSymbol -> rightSymbol mul rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.MUL);
                }),
                Map.entry("rightSymbol -> rightSymbol div rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.DIV);
                }),
                Map.entry("rightSymbol -> rightSymbol mod rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.MOD);
                }),
                Map.entry("rightSymbol -> rightSymbol add rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.ADD);
                }),
                Map.entry("rightSymbol -> rightSymbol sub rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.SUB);
                }),
                Map.entry("rightSymbol -> rightSymbol lt rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.LT);
                }),
                Map.entry("rightSymbol -> rightSymbol doubleEq rightSymbol", (rt, sonList) -> {
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.EQUAL);
                }),
                Map.entry("symbolStatementSentence -> type leftSymbol eq rightSymbol", (rt, sonList) -> {
                    String address = toString(sonList.get(1).get("address"));
                    String rightSymbolAddress = toString(sonList.get(3).get("address"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN, address, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(3).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(rightSymbolCodes, currentCode);

                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(3).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(rightSymbolScope, List.of(address));

                    rt.put("codes", codes);
                    rt.put("scope", scope);
                }),
                Map.entry("symbolUpdateSentence -> leftSymbol eq rightSymbol", (rt, sonList) -> {
                    String address = toString(sonList.get(0).get("address"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));

                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UPDATE, address, rightSymbolAddress, null));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(rightSymbolCodes, currentCode);

                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(3).get("scope"));


                    rt.put("codes", codes);
                    rt.put("scope", rightSymbolScope);
                    rt.put("address", address);
                }),
                Map.entry("returnSentence -> return rightSymbol", (rt, sonList) -> {
                    String address = toString(sonList.get(1).get("address"));
                    Collection<PavaDefaultThreeAddressCode> codes1 = toCodes(sonList.get(1).get("codes"));

                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.RETURN, address, null, null));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(codes1, currentCode);

                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(1).get("scope"));

                    rt.put("codes", codes);
                    rt.put("scope", rightSymbolScope);
                }),
                Map.entry("sentence -> symbolStatementSentence", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("sentence -> symbolUpdateSentence", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("sentence -> returnSentence", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("ifBlock -> if leftBracket rightSymbol rightBracket blockUnit", (rt, sonList) -> {
                    String endLabel = labelPrefix + tmpId[0]++;

                    // rightSymbol 的变量提升
                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(2).get("scope"));
                    List<PavaDefaultThreeAddressCode> rightSymbolScopeDefineCodes = rightSymbolScope.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.DEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));

                    List<PavaDefaultThreeAddressCode> rightSymbolScopeUndefineCodes = rightSymbolScope.stream()
                            .filter(o -> !o.equals(rightSymbolAddress))
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> jFalseCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, endLabel, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(4).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> endCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, endLabel, null, null));

                    List<PavaDefaultThreeAddressCode> rightSymbolSelfUndefineCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, rightSymbolAddress, null, null));


                    /// merge code
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            rightSymbolScopeDefineCodes,
                            rightSymbolCodes,
                            rightSymbolScopeUndefineCodes,
                            jFalseCodes,
                            blockCodes,
                            endCodes,
                            rightSymbolSelfUndefineCodes
                    );
                    rt.put("codes", codes);
                }),
                Map.entry("ifBlock -> if leftBracket rightSymbol rightBracket blockUnit else blockUnit", (rt, sonList) -> {
                    String labelFalseBegin = labelPrefix + tmpId[0]++;
                    String labelEnd = labelPrefix + tmpId[0]++;

                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));

                    Collection<PavaDefaultThreeAddressCode> jFalseCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, labelFalseBegin, rightSymbolAddress, null));

                    // true block begin label here
                    Collection<PavaDefaultThreeAddressCode> trueBlockCodes = toCodes(sonList.get(4).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> jumpEndCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, labelEnd, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> falseBlockBeginCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, labelFalseBegin, null, null));
                    Collection<PavaDefaultThreeAddressCode> falseBlockCodes = toCodes(sonList.get(6).get("codes"));
                    // jump end

                    Collection<PavaDefaultThreeAddressCode> endCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, labelEnd, null, null));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(rightSymbolCodes, jFalseCodes,
                            trueBlockCodes, jumpEndCodes, falseBlockBeginCodes, falseBlockCodes, endCodes);
                    rt.put("codes", codes);

                }),
                Map.entry("forBlock -> for leftBracket sentence semicolon rightSymbol semicolon sentence rightBracket blockUnit", (rt, sonList) -> {
                    String labelLoopBegin = labelPrefix + tmpId[0]++;
                    String labelEnd = labelPrefix + tmpId[0]++;

                    Collection<PavaDefaultThreeAddressCode> forBeginCodes = toCodes(sonList.get(2).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> loopBeginCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, labelLoopBegin, null, null));

                    Collection<PavaDefaultThreeAddressCode> forCompareCodes = toCodes(sonList.get(4).get("codes"));

                    String forCompareAddress = toString(sonList.get(4).get("address"));
                    Collection<PavaDefaultThreeAddressCode> jumpCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, labelEnd, forCompareAddress, null));

                    Collection<PavaDefaultThreeAddressCode> forBodyCodes = toCodes(sonList.get(8).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> forBodyEndCodes = toCodes(sonList.get(6).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> loopCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, labelLoopBegin, null, null));

                    Collection<PavaDefaultThreeAddressCode> forEndCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, labelEnd, null, null));


                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(forBeginCodes,
                            loopBeginCodes, forCompareCodes, jumpCodes, forBodyCodes, forBodyEndCodes, loopCodes, forEndCodes);
                    rt.put("codes", codes);
                }),
                Map.entry("whileBlock -> while leftBracket rightSymbol rightBracket blockUnit", (rt, sonList) -> {
                    String endLabel = labelPrefix + tmpId[0]++;
                    String beginLabel = labelPrefix + tmpId[0]++;

                    Collection<PavaDefaultThreeAddressCode> beginCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, beginLabel, null, null));


                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));

                    Collection<PavaDefaultThreeAddressCode> jFalseCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, endLabel, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(4).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> whileLoopCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, beginLabel, null, null));

                    Collection<PavaDefaultThreeAddressCode> endCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, endLabel, null, null));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(beginCodes,
                            rightSymbolCodes, jFalseCodes, blockCodes, whileLoopCodes, endCodes);
                    rt.put("codes", codes);

                }),
                Map.entry("blockUnit -> sentence semicolon", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("blockUnit -> ifBlock", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("blockUnit -> forBlock", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("blockUnit -> whileBlock", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("blockUnit -> leftCurlyBracket block rightCurlyBracket", (rt, sonList) -> {
                    rt.putAll(sonList.get(1));
                }),
                Map.entry("block -> blockUnit", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("block -> block block", (rt, sonList) -> {
                    Collection<PavaDefaultThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes2 = toCodes(sonList.get(1).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(codes1, codes2);
                    rt.put("codes", codes);
                }),
                Map.entry("cFileUnit -> functionStatement", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("someCFileUnit -> cFileUnit", (rt, sonList) -> {
                    rt.putAll(sonList.get(0));
                }),
                Map.entry("someCFileUnit -> someCFileUnit someCFileUnit", (rt, sonList) -> {
                    Collection<PavaDefaultThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes2 = toCodes(sonList.get(1).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(codes1, codes2);
                    rt.put("codes", codes);
                })
        );
        return toCodes(SyntaxDirectedTranslation.translation(syntaxTree, innerNodeConfig).get("codes"));
    }


}
