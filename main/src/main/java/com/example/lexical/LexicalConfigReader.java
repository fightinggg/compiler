package com.example.lexical;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.LanguageConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LexicalConfigReader {
    public static LexicalConfig read(String path, String tag) {
        byte[] code;
        try (InputStream inputStream = LexicalConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 不要使用反射
        JSONObject jsonObject = JSON.parseObject(new String(code));
        LanguageConfig languageConfig = new LanguageConfig();
        languageConfig.setName(jsonObject.getString("name"));
        languageConfig.setTarget(jsonObject.getString("target"));
        languageConfig.setBlankToken(jsonObject.getString("blankToken"));
        JSONArray tokenJsonArray = jsonObject.getJSONArray("tokens");
        languageConfig.setTokens(IntStream.range(0, tokenJsonArray.size())
                .mapToObj(tokenJsonArray::getJSONObject)
                .map(o -> o.getInnerMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().toString())))
                .collect(Collectors.toList())
        );
        //languageConfig.setProductionsTable(jsonObject.getString("blankToken"));

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
