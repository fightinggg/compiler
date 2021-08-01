package com.example.grammar;

import com.alibaba.fastjson.JSON;
import com.example.LanguageConfig;
import com.example.lexical.LexicalConfig;
import com.example.lexical.Token;
import com.example.utils.MapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author wsx
 */
public class GrammarReader {

    static Production string2Production(String from, String to, Map<String, Integer> symbolId) {
        Integer left = symbolId.get(from);
        List<Integer> right = Arrays.stream(to.split(" "))
                .filter(symbol -> !symbol.isBlank())
                .map(symbolId::get)
                .collect(Collectors.toList());
        return new ProductionImpl(left, right, from + " -> " + to);
    }

    public static GrammarConfigImpl read(String path, String tag) {
        byte[] code;
        try (InputStream inputStream = GrammarReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        LanguageConfig languageConfig = JSON.parseObject(new String(code), LanguageConfig.class);

        List<String> terminal = languageConfig.getTokens().stream().map(Map::keySet).flatMap(Collection::stream).collect(Collectors.toList());
        terminal.add(Token.END);
        terminal.add(Token.EMPTY);

        List<String> nonTerminal = languageConfig.getProductionsTable().keySet().stream().toList();

        List<String> symbols = Stream.concat(nonTerminal.stream(), terminal.stream()).collect(Collectors.toList());

        Map<String, Integer> symbolId = IntStream.range(0, symbols.size())
                .boxed()
                .map(i -> Map.entry(symbols.get(i), i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Production[] productions = languageConfig.getProductionsTable().entrySet()
                .stream()
                .flatMap(kv -> kv.getValue().stream().map(o -> Map.entry(kv.getKey(), o)))
                .map(kv -> string2Production(kv.getKey(), kv.getValue(), symbolId))
                .toArray(Production[]::new);


        return new GrammarConfigImpl(productions,
                symbolId.get(languageConfig.getTarget()),
                terminal.stream().map(symbolId::get).collect(Collectors.toList()),
                nonTerminal.stream().map(symbolId::get).collect(Collectors.toList()),
                symbols,
                languageConfig.getName() + tag);
    }
}
