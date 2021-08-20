package com.example.lang.cpp;

import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;
import com.example.utils.MergeableCollection;
import com.example.pava.impl.PavaDefaultThreeAddressCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.Map.entry;


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
        String returnAddress = "returnAddress";
        String varPrefix = "symbol_";
        String jumpLabelPrefix = "jump_label_";
        String returnLabelPrefix = "return_label_";


        final Map<String, SyntaxDirectedTranslation.SyntaxDirectedTranslationConsumer> innerNodeConfig = Map.ofEntries(
                entry("type -> symbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("typeName", sonList.get(0).get("tokenRaw"));
                }),
                entry("leftSymbol -> symbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                }),
                entry("someRightSymbolDefineJoinByCommaNotEmpty -> type rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("typeList", List.of(sonList.get(0).get("typeName")));
                    rt.put("addressList", List.of(sonList.get(1).get("address")));
                }),
                entry("someRightSymbolDefineJoinByCommaNotEmpty -> someRightSymbolDefineJoinByCommaNotEmpty comma type rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    List<String> typeList = new ArrayList<>(toStringCollection(sonList.get(0).get("typeList")));
                    List<String> addressList = new ArrayList<>(toStringCollection(sonList.get(0).get("addressList")));
                    typeList.add(toString(sonList.get(2).get("typeName")));
                    addressList.add(toString(sonList.get(3).get("address")));
                    rt.put("typeList", typeList);
                    rt.put("addressList", addressList);
                }),
                entry("functionStatement -> type symbol leftBracket rightBracket leftCurlyBracket block rightCurlyBracket", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
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
                entry("functionStatement -> type symbol leftBracket someRightSymbolDefineJoinByCommaNotEmpty rightBracket leftCurlyBracket block rightCurlyBracket", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String funcionName = toString(sonList.get(1).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> functionLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, funcionName, null, null));

                    List<String> addressList = new ArrayList<>(toStringCollection(sonList.get(3).get("addressList")));
                    Collections.reverse(addressList);
                    Collection<PavaDefaultThreeAddressCode> parmaLoadCodes = addressList.stream()
                            .map(ad -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.PARMA_LOAD, ad, null, null))
                            .toList();


                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(6).get("scope"));
                    List<PavaDefaultThreeAddressCode> rightSymbolScopeDefineCodes = rightSymbolScope.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.DEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(6).get("codes"));

                    List<PavaDefaultThreeAddressCode> rightSymbolScopeUndefineCodes = rightSymbolScope.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            functionLabelCodes,
                            parmaLoadCodes,
                            rightSymbolScopeDefineCodes,
                            blockCodes,
                            rightSymbolScopeUndefineCodes
                    );

                    rt.put("codes", codes);
                }),
                entry("someRightSymbolJoinByCommaNotEmpty -> rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String rightSymbolAddress = toString(sonList.get(0).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(0).get("codes"));

                    rt.put("addressList", List.of(rightSymbolAddress));
                    rt.put("codes", rightSymbolCodes);
                }),
                entry("someRightSymbolJoinByCommaNotEmpty -> someRightSymbolJoinByCommaNotEmpty comma rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));

                    Collection<String> oldAddress = toStringCollection(sonList.get(0).get("addressList"));
                    Collection<PavaDefaultThreeAddressCode> oldCodes = toCodes(sonList.get(0).get("codes"));

                    Collection<String> addressList = MergeableCollection.merge(oldAddress, List.of(rightSymbolAddress));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(oldCodes, rightSymbolCodes);

                    rt.put("addressList", addressList);
                    rt.put("codes", codes);
                }),
                entry("functionInvoke -> symbol leftBracket rightBracket", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = varPrefix + tmpId[0]++;
                    String functionName = toString(sonList.get(0).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> callCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.CALL, address, functionName, null));

                    rt.put("codes", callCodes);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                entry("functionInvoke -> symbol leftBracket someRightSymbolJoinByCommaNotEmpty rightBracket", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
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
                entry("rightSymbol -> symbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("codes", new ArrayList<>());
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                    rt.put("scope", List.of());
                }),
                entry("rightSymbol -> number", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = varPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                entry("rightSymbol -> functionInvoke", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("rightSymbol -> string", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = varPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_STRING, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                entry("rightSymbol -> rightSymbol mul rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.MUL);
                }),
                entry("rightSymbol -> rightSymbol div rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.DIV);
                }),
                entry("rightSymbol -> rightSymbol mod rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.MOD);
                }),
                entry("rightSymbol -> rightSymbol add rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.ADD);
                }),
                entry("rightSymbol -> rightSymbol sub rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.SUB);
                }),
                entry("rightSymbol -> rightSymbol lt rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.LT);
                }),
                entry("rightSymbol -> rightSymbol doubleEq rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(varPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.EQUAL);
                }),
                entry("symbolStatementSentence -> type leftSymbol eq rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
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
                entry("symbolUpdateSentence -> leftSymbol eq rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
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
                entry("returnSentence -> return rightSymbol", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String returnLabel = returnLabelPrefix + tmpId[0]++;
                    String address = toString(sonList.get(1).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(1).get("codes"));

                    List<PavaDefaultThreeAddressCode> currentCode = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN, returnAddress, address, null),
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, returnLabel, null, null)
                    );
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(rightSymbolCodes, currentCode);

                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(1).get("scope"));

                    rt.put("codes", codes);
                    rt.put("scope", rightSymbolScope);
                }),
                entry("sentence -> symbolStatementSentence", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("sentence -> symbolUpdateSentence", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("sentence -> returnSentence", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("ifBlock -> if leftBracket rightSymbol rightBracket blockUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String endLabel = returnLabelPrefix + tmpId[0]++;

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
                    rt.put("scope", List.of());
                }),
                entry("ifBlock -> if leftBracket rightSymbol rightBracket blockUnit else blockUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String labelFalseBegin = returnLabelPrefix + tmpId[0]++;
                    String labelEnd = returnLabelPrefix + tmpId[0]++;

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

                    Collection<PavaDefaultThreeAddressCode> jFalseCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, labelFalseBegin, rightSymbolAddress, null));

                    // true block begin label here
                    Collection<PavaDefaultThreeAddressCode> trueBlockCodes = toCodes(sonList.get(4).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> jumpEndCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, labelEnd, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> falseBlockBeginCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, labelFalseBegin, null, null));
                    Collection<PavaDefaultThreeAddressCode> falseBlockCodes = toCodes(sonList.get(6).get("codes"));
                    // jump end

                    Collection<PavaDefaultThreeAddressCode> endCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, labelEnd, null, null));


                    List<PavaDefaultThreeAddressCode> rightSymbolSelfUndefineCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, rightSymbolAddress, null, null));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            rightSymbolScopeDefineCodes,
                            rightSymbolCodes,
                            rightSymbolScopeUndefineCodes,
                            jFalseCodes,
                            trueBlockCodes,
                            jumpEndCodes,
                            falseBlockBeginCodes,
                            falseBlockCodes,
                            endCodes,
                            rightSymbolSelfUndefineCodes
                    );
                    rt.put("codes", codes);
                    rt.put("scope", List.of());

                }),
                entry("forBlock -> for leftBracket sentence semicolon rightSymbol semicolon sentence rightBracket blockUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String labelLoopBegin = returnLabelPrefix + tmpId[0]++;
                    String labelEnd = returnLabelPrefix + tmpId[0]++;

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
                    rt.put("scope", List.of());
                }),
                entry("whileBlock -> while leftBracket rightSymbol rightBracket blockUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String endLabel = returnLabelPrefix + tmpId[0]++;
                    String beginLabel = returnLabelPrefix + tmpId[0]++;

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
                    rt.put("scope", List.of());

                }),
                entry("blockUnit -> sentence semicolon", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> ifBlock", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> forBlock", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> whileBlock", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> leftCurlyBracket block rightCurlyBracket", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    // 变量提升
                    Collection<String> scope = toStringCollection(sonList.get(1).get("scope"));
                    Collection<PavaDefaultThreeAddressCode> scopeDefineCodes = scope.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.DEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(1).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> scopeUnDefineCodes = scope.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(scopeDefineCodes, blockCodes, scopeUnDefineCodes);

                    rt.put("codes", codes);
                    rt.put("scope", List.of());
                }),
                entry("block -> blockUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("block -> block block", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Collection<PavaDefaultThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes2 = toCodes(sonList.get(1).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(codes1, codes2);

                    Collection<String> scope1 = toStringCollection(sonList.get(0).get("scope"));
                    Collection<String> scope2 = toStringCollection(sonList.get(1).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(scope1, scope2);

                    rt.put("codes", codes);
                    rt.put("scope", scope);
                }),
                entry("cFileUnit -> functionStatement", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("someCFileUnit -> cFileUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("someCFileUnit -> someCFileUnit someCFileUnit", (rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    Collection<PavaDefaultThreeAddressCode> codes1 = toCodes(sonList.get(0).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes2 = toCodes(sonList.get(1).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(codes1, codes2);
                    rt.put("codes", codes);
                })
        );

        Map<SyntaxTree.Node, Map<String, Object>> translation = SyntaxDirectedTranslation.translation(new HashMap<>(), syntaxTree, innerNodeConfig);

        return toCodes(translation.get(syntaxTree.getRoot()).get("codes"));
    }


}
