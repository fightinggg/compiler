package com.example.lexical;

import com.alibaba.fastjson.JSON;
import com.example.LanguageConfig;

import java.io.IOException;
import java.io.InputStream;

public class LexicalConfigReader {
    public static LexicalConfig read(String path) {
        byte[] code;
        try (InputStream inputStream = LexicalConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        LanguageConfig languageConfig = JSON.parseObject(new String(code), LanguageConfig.class);

        return new LexicalConfigImpl(languageConfig.getTokens());
    }
}
