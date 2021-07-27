package com.example.lang.reg;

import com.example.grammar.GrammarFollowSet;
import com.example.lexical.LexicalAnalysis;
import com.example.lexical.LexicalConfig;
import com.example.lexical.Token;
import com.example.lexical.TokenImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 这一块负责正则文法的词法分析
 * 由于通用的词法分析器依赖于正则文法，所以正则文法的词法分析器就没办法依赖自己了
 */
public class RegLexicalAnalysisImpl implements LexicalAnalysis {

    @Override
    public List<Token> parsing(String code, LexicalConfig lexicalConfig) {
        Map<Character, String> map = new HashMap<>();

        Arrays.stream("0123456789".split("")).forEach(o -> map.put(o.charAt(0), "number"));
        Arrays.stream("abcdefghijklmnopqrstuvwxyz".split("")).forEach(o -> map.put(o.charAt(0), "lowerCaseLetter"));
        Arrays.stream("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("")).forEach(o -> map.put(o.charAt(0), "upperCaseLetter"));
        Arrays.stream("`!@#$%^&_|:\"<>=\\;',/".split("")).forEach(o -> map.put(o.charAt(0), "specialChar"));
        map.put('(', "leftBracket");
        map.put(')', "rightBracket");
        map.put('|', "or");
        map.put('[', "leftSquareBracket");
        map.put(']', "rightSquareBracket");
        map.put('{', "leftCurlyBracket");
        map.put('}', "rightCurlyBracket");
        map.put('*', "star");
        map.put('+', "add");
        map.put('?', "questionMark");
        map.put('-', "sub");
        map.put('.', "dot");

        Map<Character, String> escapeMap = Map.ofEntries(
                Map.entry('s', "blankSet"),
                Map.entry('w', "letterSet"),
                Map.entry('d', "simpleNumberSet")
        );

        List<Token> list = new ArrayList<>();
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '\\') {
                i++;
                list.add(new TokenImpl(escapeMap.getOrDefault(code.charAt(i), "escape"),
                        String.valueOf(code.charAt(i))));
            } else {
                list.add(new TokenImpl(map.get(code.charAt(i)), String.valueOf(code.charAt(i))));
            }
        }
        list.add(new TokenImpl(Token.END, Token.END));

        return list.stream().toList();
    }
}
