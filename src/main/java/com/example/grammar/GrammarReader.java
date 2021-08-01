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

    static Production string2Production(String from, String to, Map<String, Integer> symbolId, GrammarConfig grammarConfig) {
        Integer left = symbolId.get(from);
        List<Integer> right = Arrays.stream(to.split(" "))
                .filter(symbol -> !symbol.isBlank())
                .map(symbolId::get)
                .collect(Collectors.toList());
        return new ProductionImpl(left, right, grammarConfig);
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


        GrammarConfigImpl grammarConfig = new GrammarConfigImpl();
        grammarConfig.setTarget(symbolId.get(languageConfig.getTarget()));
        grammarConfig.setTerminal(terminal.stream().map(symbolId::get).collect(Collectors.toList()));
        grammarConfig.setNonTerminal(nonTerminal.stream().map(symbolId::get).collect(Collectors.toList()));
        grammarConfig.setName(languageConfig.getName() + tag);
        grammarConfig.setSymbols(symbols);

        List<Integer> emptyTerminals = grammarConfig.getTerminal().stream().filter(o -> symbols.get(o).equals("")).collect(Collectors.toList());
        if (emptyTerminals.size() != 1) {
            throw new RuntimeException("终结符中缺少 empty");
        }
        grammarConfig.setEmptyTerminal(emptyTerminals.get(0));

        List<Integer> endTerminals = grammarConfig.getTerminal().stream().filter(o -> symbols.get(o).equals(Token.END)).collect(Collectors.toList());
        if (endTerminals.size() != 1) {
            throw new RuntimeException("终结符中缺少 end");
        }
        grammarConfig.setEndTerminal(endTerminals.get(0));

        grammarConfig.setProductions(languageConfig.getProductionsTable().entrySet()
                .stream()
                .flatMap(kv -> kv.getValue().stream().map(o -> Map.entry(kv.getKey(), o)))
                .map(kv -> string2Production(kv.getKey(), kv.getValue(), symbolId, grammarConfig))
                .toArray(Production[]::new));
        grammarConfig.setProductionIdMap(IntStream.range(0, grammarConfig.getProductions().length).boxed().collect(Collectors.toMap(i -> grammarConfig.getProductions()[i], i -> i)));


        return grammarConfig;

    }
}
