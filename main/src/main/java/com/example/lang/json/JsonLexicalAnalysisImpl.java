package com.example.lang.json;


import com.example.lang.reg.Reg;
import com.example.lexical.LexicalAnalysis;
import com.example.lexical.LexicalConfig;
import com.example.lexical.Token;
import com.example.lexical.TokenImpl;
import com.example.nfa.*;
import com.example.visiable.FileUtils;
import com.example.visiable.NfaVisiable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonLexicalAnalysisImpl implements LexicalAnalysis {
    @Override
    public List<Token> parsing(String code, LexicalConfig lexicalConfig) {
        Map<Character, String> map = new HashMap<>();

        map.put('[', "leftSquareBracket");
        map.put(']', "rightSquareBracket");
        map.put('{', "leftCurlyBracket");
        map.put('}', "rightCurlyBracket");
        map.put(':', "colon");
        map.put(',', "comma");
        map.put(' ', "blank");
        map.put('\n', "blank");
        map.put('\t', "blank");

        List<Token> list = new ArrayList<>();
        for (int i = 0; i < code.length(); i++) {
            if (map.containsKey(code.charAt(i))) {
                list.add(new TokenImpl(map.get(code.charAt(i)), String.valueOf(code.charAt(i))));
            } else if (code.charAt(i) == '"') {
                StringBuilder stringBuilder = new StringBuilder();
                for (i++; i < code.length() && code.charAt(i) != '"'; i++) {
                    if (code.charAt(i) == '\\') {
                        i++;
                    }
                    if (i >= code.length()) {
                        throw new RuntimeException("could parse json code:\n" + code);
                    }
                    stringBuilder.append(code.charAt(i));
                }
                list.add(new TokenImpl("string", stringBuilder.toString()));
            } else {
                throw new RuntimeException("could parse json code:\n" + code.substring(0, i + 1));
            }
        }
        list.add(new TokenImpl(Token.END, Token.END));

        return list.stream().filter(o -> !o.type().equals(lexicalConfig.getBlankToken())).collect(Collectors.toList());
    }
}
