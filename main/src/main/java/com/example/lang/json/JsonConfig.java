package com.example.lang.json;

import com.example.config.LanguageConfig;

import java.util.List;
import java.util.Map;

public class JsonConfig {

    public static LanguageConfig getLanguageConfig() {
        LanguageConfig languageConfig = new LanguageConfig();

        languageConfig.setName("json");
        languageConfig.setTarget("target");
        languageConfig.setBlankToken("blank");
        languageConfig.setTokens(List.of(
                Map.of("string", "\"[\\s\\w\\d]*\""),
                Map.of("number", "0|[123456789]\\d*"),
                Map.of("colon", ":"),
                Map.of("leftSquareBracket", "\\["),
                Map.of("rightSquareBracket", "\\]"),
                Map.of("leftCurlyBracket", "{"),
                Map.of("rightCurlyBracket", "}"),
                Map.of("comma", ",")
        ));
        languageConfig.setProductionsTable(Map.ofEntries(
                Map.entry("target", List.of(
                        "object"
                )),
                Map.entry("object", List.of(
                        "string",
                        "number",
                        "array",
                        "leftCurlyBracket kvs rightCurlyBracket",
                        "leftCurlyBracket rightCurlyBracket"
                )),
                Map.entry("array", List.of(
                        "leftSquareBracket objects rightSquareBracket",
                        "leftSquareBracket rightSquareBracket"
                )),
                Map.entry("kv", List.of(
                        "string colon object"
                )),
                Map.entry("kvs", List.of(
                        "kv",
                        "kvs comma kv"
                )),
                Map.entry("objects", List.of(
                        "object",
                        "objects comma object"
                ))
        ));
        return languageConfig;
    }


}
