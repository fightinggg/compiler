package com.example.grammar;

import com.alibaba.fastjson.JSON;
import com.example.LanguageConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wsx
 */
public class GrammarReader {

    static List<String> string2List(String s) {
        if (s.isEmpty()) {
            return new ArrayList<>();
        } else if (s.length() == 1) {
            return new ArrayList<>(List.of(s));
        } else {
            return new ArrayList<>(Arrays.asList(s.split(" ")));
        }
    }

    public static GrammarConfigImpl read(String path) {
        byte[] code;
        try (InputStream inputStream = GrammarReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        LanguageConfig languageConfig = JSON.parseObject(new String(code), LanguageConfig.class);


        Map<String, Set<Production>> productions = languageConfig.getProductionsTable()
                .entrySet()
                .stream()
                .flatMap(o -> o.getValue().stream().map(v -> new ProductionImpl(o.getKey(), string2List(v))))
                .collect(Collectors.groupingBy(ProductionImpl::leftSymbol, Collectors.toSet()));

        Set<String> symbol = languageConfig.getProductionsTable().values()
                .stream()
                .flatMap(Collection::stream)
                .flatMap(o -> Arrays.stream(o.split(" ")))
                .collect(Collectors.toSet());

        Set<String> notTerminal = languageConfig.getProductionsTable().keySet();

        symbol.removeAll(notTerminal);


        return new GrammarConfigImpl(productions, languageConfig.getTarget(), symbol);
    }
}
