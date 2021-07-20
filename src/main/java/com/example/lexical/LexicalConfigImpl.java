package com.example.lexical;

import java.util.Map;

public class LexicalConfigImpl implements LexicalConfig {
    Map<String, String> tokens;

    LexicalConfigImpl(Map<String, String> tokens) {
        this.tokens = tokens;
    }

    @Override
    public Map<String, String> getTokens() {
        return tokens;
    }
}
