package com.example.lexical;

import com.alibaba.fastjson.JSON;
import com.example.LanguageConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LexicalConfigReader {
    public static LexicalConfig read(String path, String tag) {
        byte[] code;
        try (InputStream inputStream = LexicalConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LanguageConfig languageConfig = JSON.parseObject(new String(code), LanguageConfig.class);

        final List<Map<String, String>> tokens = languageConfig.getTokens();

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
