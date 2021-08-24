package com.example.lexical;

import com.example.config.LanguageConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LexicalConfigReader {
    public static LexicalConfig read(LanguageConfig languageConfig, String tag) {
        final List<Map<String, String>> tokens = new ArrayList<>(languageConfig.getTokens());

        final Map<String, String> tokenMap = tokens.stream()
                .flatMap(o -> o.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Map<String, Integer> tokenOrder = IntStream.range(0, tokens.size())
                .boxed()
                .map(i -> Map.entry(tokens.get(i).keySet().iterator().next(), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new LexicalConfigImpl(
                tokenMap,
                tokenOrder,
                languageConfig.getBlankToken(),
                languageConfig.getName() + tag
        );
    }
}
