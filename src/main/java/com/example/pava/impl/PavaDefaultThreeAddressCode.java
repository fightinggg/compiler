package com.example.pava.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Pava语言支持多套指令集， Pava语言的指令集之一<br><br/>
 * 也是Pava默认的指令集
 */
@Getter
@AllArgsConstructor
public class PavaDefaultThreeAddressCode {
    public static final String MUL = "mul";
    public static final String DIV = "div";
    public static final String MOD = "mod";
    public static final String ASSIGN = "assign";
    public static final String ASSIGN_NUMBER = "assignNumber";
    public static final String ADD = "add";
    public static final String SUB = "sub";
    public static final String EQUAL = "equal";
    public static final String RETURN = "return";
    public static final String LABEL = "label";
    public static final String JFALSE = "jFalse";
    public static final String JUMP = "jump";
    public static final String SAVE_TO_STACK = "saveToStack";
    public static final String LOAD_FROM_STACK = "loadFromStack";
    public static final String SAVE_ALL_REG_TO_STACK = "saveAllRegToStack";
    public static final String CLEAR_REG = "clearReg";
    public static final String LOAD_ALL_REG_FROM_STACK = "loadAllFromStack";
    public static final String LT = "lt";
    public static final String ASSIGN_STRING = "assignString";
    public static final String UPDATE = "update";
    public static final String DEFINE_REG = "defineSymbol";
    public static final String UNDEFINE_SYMBOL = "undefineSymbol";
    public static final String JUMP_REG = "jumpReg";


    private String operator;
    private String target;
    private String op1;
    private String op2;

    public static class Reg {
        public static final String returnValueRegisterName = "pvmReg_returnValue"; // 储存返回值的寄存器
        public static final String returnJumpRegister = "saveReg_returnJump"; // 储存函数return跳转的寄存器
    }


    @Override
    public String toString() {
        return "%-20s %-20s %-20s %-20s".formatted(operator, target, op1, op2);
    }

    public static PavaDefaultThreeAddressCode decode(String code) {
        List<String> s = Arrays.stream(code.split(" ")).filter(o -> o.length() > 0).toList();
        return new PavaDefaultThreeAddressCode(s.get(0), s.get(1), s.get(2), s.get(3));
    }
}