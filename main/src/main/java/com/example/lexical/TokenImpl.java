package com.example.lexical;

import com.example.utils.ToStringUtils;

public class TokenImpl implements Token {
    private final String type;
    private final String raw;

    public TokenImpl(String type, String raw) {
        this.type = type;
        this.raw = raw;
    }

    @Override
    public String toString() {
        return "%s(%s)".formatted(ToStringUtils.toString(raw), type);
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String raw() {
        return raw;
    }
}
