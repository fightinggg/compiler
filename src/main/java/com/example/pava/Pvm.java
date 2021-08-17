package com.example.pava;

/**
 * Pvm 是Pava语言解释器，可以用于解释运行Pava语言
 */
public interface Pvm<T> {
    /**
     * pava语言核心代码
     *
     * @param pavaCode pava字节码
     * @param args     pava程序参数
     * @return 返回值
     */
    int run(PavaCode<T> pavaCode, String[] args);
}
