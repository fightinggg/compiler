package com.example.grammar;

import java.util.List;

/**
 * 这个对象是不可变对象
 * 产生式
 *
 * @author wsx
 */
public interface Production {
    Integer leftSymbol();

    List<Integer> rightSymbol();

    String raw();

    GrammarConfig grammarConfig();
}
