package com.example.pava;

/**
 * Pava是一款跨平台语言，运行于Pvm虚拟机
 * <p>
 * Pava语言的目的是统一所有其他语言，在未来，任何语言的代码都可以编译为Pava字节码，并交与Pvm运行
 */
public interface PavaCode<T> {
    String type(); // Pava 字节码类型

    String version(); // Pava 字节码版本

    T pavaCode(); // Pava 字节码内容
}
