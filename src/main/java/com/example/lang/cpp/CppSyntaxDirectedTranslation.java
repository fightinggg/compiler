package com.example.lang.cpp;

import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;
import com.example.utils.MergeableCollection;
import com.example.pava.impl.PavaDefaultThreeAddressCode;

import java.util.*;
import java.util.stream.IntStream;

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
        String saveRegisterPrefix = "saveReg_"; // 保存寄存器
        String labelPrefix = "label_";
        String returnLabelPrefix = "returnLabel_";


        final Map<String, SyntaxDirectedTranslation.SyntaxDirectedTranslationConsumer> innerNodeConfig = Map.ofEntries(
                entry("type -> symbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("typeName", sonList.get(0).get("tokenRaw"));
                }),
                entry("leftSymbol -> symbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                }),
                entry("someRightSymbolDefineJoinByCommaNotEmpty -> type rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("typeList", List.of(sonList.get(0).get("typeName")));
                    rt.put("addressList", List.of(sonList.get(1).get("address")));
                }),
                entry("someRightSymbolDefineJoinByCommaNotEmpty -> someRightSymbolDefineJoinByCommaNotEmpty comma type rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    List<String> typeList = new ArrayList<>(toStringCollection(sonList.get(0).get("typeList")));
                    List<String> addressList = new ArrayList<>(toStringCollection(sonList.get(0).get("addressList")));
                    typeList.add(toString(sonList.get(2).get("typeName")));
                    addressList.add(toString(sonList.get(3).get("address")));
                    rt.put("typeList", typeList);
                    rt.put("addressList", addressList);
                }),
                entry("functionStatement -> type symbol leftBracket rightBracket leftCurlyBracket block rightCurlyBracket", (fa, rt, sonList, accessAllSon) -> {
                    String functionExitAndReturnCodeLabel = returnLabelPrefix + tmpId[0]++;
                    rt.put("functionExitAndReturnCodeLabel", functionExitAndReturnCodeLabel);

                    accessAllSon.run();

                    List<String> parmasAddressList = new ArrayList<>(); //没有参数

                    String funcionName = toString(sonList.get(1).get("tokenRaw"));

                    Collection<String> allFunctionSymbol = MergeableCollection.merge(toStringCollection(sonList.get(5).get("scope")), parmasAddressList);


                    Collection<PavaDefaultThreeAddressCode> functionLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, funcionName, null, null));


                    Collection<PavaDefaultThreeAddressCode> defineAllFunctionSymbol = allFunctionSymbol.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.DEFINE_REG, o, null, null))
                            .toList();


                    Collection<PavaDefaultThreeAddressCode> loadReturnJumpRegister = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LOAD_FROM_STACK, PavaDefaultThreeAddressCode.Reg.returnJumpRegister, null, null)
                    );

                    Collection<PavaDefaultThreeAddressCode> loadParmas = IntStream.range(1 - parmasAddressList.size(), 1).map(o -> -o).boxed()
                            .map(parmasAddressList::get)
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LOAD_FROM_STACK, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(5).get("codes"));


                    Collection<PavaDefaultThreeAddressCode> functionExitAndReturnLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, functionExitAndReturnCodeLabel, null, null));


                    Collection<PavaDefaultThreeAddressCode> undefineAllFunctionSymbol = allFunctionSymbol.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> returnJump = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP_REG, PavaDefaultThreeAddressCode.Reg.returnJumpRegister, null, null)
                    );

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            functionLabelCodes,
                            defineAllFunctionSymbol,
                            loadReturnJumpRegister,
                            loadParmas,
                            blockCodes,
                            functionExitAndReturnLabelCodes,
                            undefineAllFunctionSymbol,
                            returnJump
                    );

                    rt.put("codes", codes);
                }),
                entry("functionStatement -> type symbol leftBracket someRightSymbolDefineJoinByCommaNotEmpty rightBracket leftCurlyBracket block rightCurlyBracket", (fa, rt, sonList, accessAllSon) -> {
                    String functionExitAndReturnCodeLabel = returnLabelPrefix + tmpId[0]++;
                    rt.put("functionExitAndReturnCodeLabel", functionExitAndReturnCodeLabel);

                    accessAllSon.run();

                    List<String> parmasAddressList = toStringCollection(sonList.get(3).get("addressList")).stream().toList();

                    String funcionName = toString(sonList.get(1).get("tokenRaw"));

                    Collection<String> allFunctionSymbol = MergeableCollection.merge(toStringCollection(sonList.get(6).get("scope")), parmasAddressList);


                    Collection<PavaDefaultThreeAddressCode> functionLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, funcionName, null, null));


                    Collection<PavaDefaultThreeAddressCode> defineAllFunctionSymbol = allFunctionSymbol.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.DEFINE_REG, o, null, null))
                            .toList();


                    Collection<PavaDefaultThreeAddressCode> loadReturnJumpRegister = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LOAD_FROM_STACK, PavaDefaultThreeAddressCode.Reg.returnJumpRegister, null, null)
                    );

                    Collection<PavaDefaultThreeAddressCode> loadParmas = IntStream.range(1 - parmasAddressList.size(), 1).map(o -> -o).boxed()
                            .map(parmasAddressList::get)
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LOAD_FROM_STACK, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(6).get("codes"));


                    Collection<PavaDefaultThreeAddressCode> functionExitAndReturnLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, functionExitAndReturnCodeLabel, null, null));


                    Collection<PavaDefaultThreeAddressCode> undefineAllFunctionSymbol = allFunctionSymbol.stream()
                            .map(o -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, o, null, null))
                            .toList();

                    Collection<PavaDefaultThreeAddressCode> returnJump = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP_REG, PavaDefaultThreeAddressCode.Reg.returnJumpRegister, null, null)
                    );

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            functionLabelCodes,
                            defineAllFunctionSymbol,
                            loadReturnJumpRegister,
                            loadParmas,
                            blockCodes,
                            functionExitAndReturnLabelCodes,
                            undefineAllFunctionSymbol,
                            returnJump
                    );

                    rt.put("codes", codes);
                }),
                entry("someRightSymbolJoinByCommaNotEmpty -> rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String rightSymbolAddress = toString(sonList.get(0).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(0).get("codes"));

                    rt.put("addressList", List.of(rightSymbolAddress));
                    rt.put("codes", rightSymbolCodes);
                    rt.put("scope", sonList.get(0).get("scope"));
                }),
                entry("someRightSymbolJoinByCommaNotEmpty -> someRightSymbolJoinByCommaNotEmpty comma rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));

                    Collection<String> oldAddress = toStringCollection(sonList.get(0).get("addressList"));
                    Collection<PavaDefaultThreeAddressCode> oldCodes = toCodes(sonList.get(0).get("codes"));

                    Collection<String> addressList = MergeableCollection.merge(oldAddress, List.of(rightSymbolAddress));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(oldCodes, rightSymbolCodes);

                    Collection<String> scope1 = toStringCollection(sonList.get(0).get("scope"));
                    Collection<String> scope2 = toStringCollection(sonList.get(1).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(scope1, scope2);

                    rt.put("addressList", addressList);
                    rt.put("codes", codes);
                    rt.put("scope", scope);
                }),
                entry("functionInvoke -> symbol leftBracket rightBracket", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = saveRegisterPrefix + tmpId[0]++;
                    String returnJumpLabel = labelPrefix + tmpId[0]++;
                    String returnJumpLabelAddress = saveRegisterPrefix + tmpId[0]++;
                    String functionName = toString(sonList.get(0).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> parmaCodes = new ArrayList<>(); // 没有代码

                    Collection<PavaDefaultThreeAddressCode> allRegisterToStack = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.SAVE_ALL_REG_TO_STACK, null, null, null)
                    );

                    Collection<PavaDefaultThreeAddressCode> parmaToStackCodes = new ArrayList<>(); //没有参数


                    Collection<PavaDefaultThreeAddressCode> returnJumpLabelToStack = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_STRING, returnJumpLabelAddress, returnJumpLabel, null),
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.SAVE_TO_STACK, returnJumpLabelAddress, null, null)
                    );


                    Collection<PavaDefaultThreeAddressCode> callCodes = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.CLEAR_REG, functionName, null, null), // 调用
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, functionName, null, null), // 调用
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, returnJumpLabel, null, null),// 返回标记
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LOAD_ALL_REG_FROM_STACK, null, null, null),
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN, address, PavaDefaultThreeAddressCode.Reg.returnValueRegisterName, null)// 返回
                    );

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            parmaCodes,
                            allRegisterToStack,
                            parmaToStackCodes,
                            returnJumpLabelToStack,
                            callCodes
                    );

                    Collection<String> parmasScope = toStringCollection(sonList.get(2).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(parmasScope, List.of(address, returnJumpLabelAddress));

                    rt.put("codes", codes);
                    rt.put("address", address);
                    rt.put("scope", scope);
                }),
                entry("functionInvoke -> symbol leftBracket someRightSymbolJoinByCommaNotEmpty rightBracket", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = saveRegisterPrefix + tmpId[0]++;
                    String returnJumpLabel = labelPrefix + tmpId[0]++;
                    String returnJumpLabelAddress = saveRegisterPrefix + tmpId[0]++;
                    String functionName = toString(sonList.get(0).get("tokenRaw"));

                    Collection<PavaDefaultThreeAddressCode> parmaCodes = toCodes(sonList.get(2).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> allRegisterToStack = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.SAVE_ALL_REG_TO_STACK, null, null, null)
                    );

                    Collection<PavaDefaultThreeAddressCode> parmaToStackCodes = toStringCollection(sonList.get(2).get("addressList")).stream()
                            .map(ad -> new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.SAVE_TO_STACK, ad, null, null))
                            .toList();


                    Collection<PavaDefaultThreeAddressCode> returnJumpLabelToStack = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_STRING, returnJumpLabelAddress, returnJumpLabel, null),
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.SAVE_TO_STACK, returnJumpLabelAddress, null, null)
                    );


                    Collection<PavaDefaultThreeAddressCode> callCodes = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.CLEAR_REG, functionName, null, null), // 调用
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, functionName, null, null), // 调用
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, returnJumpLabel, null, null),// 返回标记
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LOAD_ALL_REG_FROM_STACK, null, null, null),
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN, address, PavaDefaultThreeAddressCode.Reg.returnValueRegisterName, null)// 返回
                    );

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            parmaCodes,
                            allRegisterToStack,
                            parmaToStackCodes,
                            returnJumpLabelToStack,
                            callCodes
                    );

                    Collection<String> parmasScope = toStringCollection(sonList.get(2).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(parmasScope, List.of(address, returnJumpLabelAddress));

                    rt.put("codes", codes);
                    rt.put("address", address);
                    rt.put("scope", scope);
                }),
                entry("rightSymbol -> symbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.put("codes", new ArrayList<>());
                    rt.put("address", sonList.get(0).get("tokenRaw"));
                    rt.put("scope", List.of());
                }),
                entry("rightSymbol -> number", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = saveRegisterPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                entry("rightSymbol -> functionInvoke", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("rightSymbol -> string", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = saveRegisterPrefix + tmpId[0]++;
                    String ad = toString(sonList.get(0).get("tokenRaw"));
                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN_STRING, address, ad, null));

                    rt.put("codes", currentCode);
                    rt.put("address", address);
                    rt.put("scope", List.of(address));
                }),
                entry("rightSymbol -> rightSymbol mul rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.MUL);
                }),
                entry("rightSymbol -> rightSymbol div rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.DIV);
                }),
                entry("rightSymbol -> rightSymbol mod rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.MOD);
                }),
                entry("rightSymbol -> rightSymbol add rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.ADD);
                }),
                entry("rightSymbol -> rightSymbol sub rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.SUB);
                }),
                entry("rightSymbol -> rightSymbol lt rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.LT);
                }),
                entry("rightSymbol -> rightSymbol doubleEq rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    binaryOperator(saveRegisterPrefix, tmpId, rt, sonList, PavaDefaultThreeAddressCode.EQUAL);
                }),
                entry("symbolStatementSentence -> type leftSymbol eq rightSymbol", (fa, rt, sonList, accessAllSon) -> {
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
                entry("symbolUpdateSentence -> leftSymbol eq rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String address = toString(sonList.get(0).get("address"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));

                    List<PavaDefaultThreeAddressCode> currentCode = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.UPDATE, address, rightSymbolAddress, null));
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(rightSymbolCodes, currentCode);

                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(2).get("scope"));


                    rt.put("codes", codes);
                    rt.put("scope", rightSymbolScope);
                    rt.put("address", address);
                }),
                entry("returnSentence -> return rightSymbol", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String returnLabel = toString(fa.get("functionExitAndReturnCodeLabel"));
                    String address = toString(sonList.get(1).get("address"));
                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(1).get("codes"));

                    List<PavaDefaultThreeAddressCode> currentCode = List.of(
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.ASSIGN, PavaDefaultThreeAddressCode.Reg.returnValueRegisterName, address, null),
                            new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, returnLabel, null, null)
                    );
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(rightSymbolCodes, currentCode);

                    Collection<String> rightSymbolScope = toStringCollection(sonList.get(1).get("scope"));

                    rt.put("codes", codes);
                    rt.put("scope", rightSymbolScope);
                }),
                entry("sentence -> symbolStatementSentence", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("sentence -> symbolUpdateSentence", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("sentence -> returnSentence", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("ifBlock -> if leftBracket rightSymbol rightBracket blockUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String endLabel = returnLabelPrefix + tmpId[0]++;

                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));

                    Collection<PavaDefaultThreeAddressCode> jFalseCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, endLabel, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(4).get("codes"));

                    Collection<PavaDefaultThreeAddressCode> endCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, endLabel, null, null));

                    /// merge code
                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            rightSymbolCodes,
                            jFalseCodes,
                            blockCodes,
                            endCodes
                    );

                    Collection<String> conditonScope = toStringCollection(sonList.get(2).get("scope"));
                    Collection<String> blockScope = toStringCollection(sonList.get(4).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(conditonScope, blockScope);

                    rt.put("codes", codes);
                    rt.put("scope", scope);
                }),
                entry("ifBlock -> if leftBracket rightSymbol rightBracket blockUnit else blockUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String labelFalseBegin = returnLabelPrefix + tmpId[0]++;
                    String labelEnd = returnLabelPrefix + tmpId[0]++;

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

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            rightSymbolCodes,
                            jFalseCodes,
                            trueBlockCodes,
                            jumpEndCodes,
                            falseBlockBeginCodes,
                            falseBlockCodes,
                            endCodes
                    );

                    Collection<String> conditonScope = toStringCollection(sonList.get(2).get("scope"));
                    Collection<String> block1Scope = toStringCollection(sonList.get(4).get("scope"));
                    Collection<String> block2Scope = toStringCollection(sonList.get(6).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(conditonScope, block1Scope, block2Scope);

                    rt.put("codes", codes);
                    rt.put("scope", scope);

                }),
                entry("forBlock -> for leftBracket sentence semicolon rightSymbol semicolon sentence rightBracket blockUnit", (fa, rt, sonList, accessAllSon) -> {
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
                entry("whileBlock -> while leftBracket rightSymbol rightBracket blockUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    String endLabel = returnLabelPrefix + tmpId[0]++;
                    String beginLabel = returnLabelPrefix + tmpId[0]++;

                    Collection<PavaDefaultThreeAddressCode> beginLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, beginLabel, null, null));


                    Collection<PavaDefaultThreeAddressCode> rightSymbolCodes = toCodes(sonList.get(2).get("codes"));
                    String rightSymbolAddress = toString(sonList.get(2).get("address"));

                    Collection<PavaDefaultThreeAddressCode> jFalseCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JFALSE, endLabel, rightSymbolAddress, null));

                    Collection<PavaDefaultThreeAddressCode> blockCodes = toCodes(sonList.get(4).get("codes"));
                    Collection<PavaDefaultThreeAddressCode> whileLoopCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.JUMP, beginLabel, null, null));

                    Collection<PavaDefaultThreeAddressCode> endLabelCodes = List.of(new PavaDefaultThreeAddressCode(PavaDefaultThreeAddressCode.LABEL, endLabel, null, null));

                    Collection<PavaDefaultThreeAddressCode> codes = MergeableCollection.merge(
                            beginLabelCodes,
                            rightSymbolCodes,
                            jFalseCodes,
                            blockCodes,
                            whileLoopCodes,
                            endLabelCodes
                    );

                    Collection<String> scopeRightSymbol = toStringCollection(sonList.get(2).get("scope"));
                    Collection<String> scopeBlock = toStringCollection(sonList.get(4).get("scope"));
                    Collection<String> scope = MergeableCollection.merge(scopeRightSymbol, scopeBlock);

                    rt.put("codes", codes);
                    rt.put("scope", scope);

                }),
                entry("blockUnit -> sentence semicolon", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> ifBlock", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> forBlock", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> whileBlock", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("blockUnit -> leftCurlyBracket block rightCurlyBracket", (fa, rt, sonList, accessAllSon) -> {
                    // TODO 作用域
                    accessAllSon.run();
                    rt.putAll(sonList.get(1));
                }),
                entry("block -> blockUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("block -> block block", (fa, rt, sonList, accessAllSon) -> {
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
                entry("cFileUnit -> functionStatement", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("someCFileUnit -> cFileUnit", (fa, rt, sonList, accessAllSon) -> {
                    accessAllSon.run();
                    rt.putAll(sonList.get(0));
                }),
                entry("someCFileUnit -> someCFileUnit someCFileUnit", (fa, rt, sonList, accessAllSon) -> {
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
