package com.example.config;

import com.example.lang.json.Json;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Reader {
    public static LanguageConfig read(String path) {
        byte[] code;
        try (InputStream inputStream = new FileInputStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Object jsonObj = Json.parse(new String(code), "json");
        Map<String, ?> json = (Map<String, ?>) jsonObj;
        LanguageConfig languageConfig = new LanguageConfig();
        languageConfig.setBlankToken((String) json.get("blankToken"));
        languageConfig.setName((String) json.get("name"));
        languageConfig.setTarget((String) json.get("target"));
        languageConfig.setProductionsTable((Map<String, Collection<String>>) json.get("productionsTable"));
        languageConfig.setTokens((Collection<Map<String, String>>) json.get("tokens"));
        return languageConfig;
    }
}
