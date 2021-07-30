package com.example.lexical;

import java.util.Map;

public interface LexicalConfig {
    String name();

    Map<String, String> getTokens();

    Integer tokenOrder(String type);

    String getBlankToken();
}
