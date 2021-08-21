package com.example.lexical;

public interface Token {
    String END = "$END";
    String EMPTY = "";

    String type();

    String raw();
}
