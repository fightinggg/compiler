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
    public static final String CALL = "call";
    public static final String PARMA_PUT = "parmaPut";
    public static final String PARMA_LOAD = "parmaLoad";
    public static final String LT = "lt";
    public static final String ASSIGN_STRING = "assignString";
    public static final String UPDATE = "update";
    private String operator;
    private String target;
    private String op1;
    private String op2;

    @Override
    public String toString() {
        return "%-20s %-10s %-10s %-10s".formatted(operator, target, op1, op2);
    }

    public static PavaDefaultThreeAddressCode decode(String code) {
        List<String> s = Arrays.stream(code.split(" ")).filter(Objects::nonNull).toList();
        return new PavaDefaultThreeAddressCode(s.get(0), s.get(1), s.get(2), s.get(3));
    }
}