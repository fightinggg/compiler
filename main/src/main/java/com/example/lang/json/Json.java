package com.example.lang.json;

import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lang.Lang;

import java.util.Map;

public class Json {
    public static Object parse(String code, String tag) {
        return Lang.parse(
                JsonConfig.getLanguageConfig(),
                new SLRTableAnalyzer(),
                new LRAnalyzerImpl(),
                new JsonLexicalAnalysisImpl(),
                new JsonSyntaxDirectedTranslation(),
                code,
                tag
        );
    }
}
