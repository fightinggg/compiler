package com.example.lexical;

import java.util.Map;

public interface LexicalConfig {
    Map<String, String> getTokens();

    Integer tokenOrder(String type);

    String getBlankToken();
}
