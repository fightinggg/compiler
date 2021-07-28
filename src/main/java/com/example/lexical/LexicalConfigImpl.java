package com.example.lexical;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class LexicalConfigImpl implements LexicalConfig {
    Map<String, String> tokens;
    Map<String, Integer> tokenOrders;

    @Override
    public Map<String, String> getTokens() {
        return tokens;
    }

    @Override
    public Integer tokenOrder(String type) {
        return tokenOrders.get(type);
    }
}
