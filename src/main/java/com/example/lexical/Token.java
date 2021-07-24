package com.example.lexical;

public interface Token {
    public static final String END = "$END";

    String type();

    String raw();
}
