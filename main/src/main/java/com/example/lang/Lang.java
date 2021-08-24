package com.example.lang;

import com.example.config.LanguageConfig;
import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzer;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.lang.cpp.CppSyntaxDirectedTranslation;
import com.example.lexical.*;
import com.example.pava.impl.PavaDefaultThreeAddressCode;
import com.example.sdt.SyntaxDirectedTranslation;
import com.example.syntaxtree.SyntaxTree;
import com.example.visiable.FileUtils;
import com.example.visiable.SyntaxTreeVisiable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lang {
    public static <T> T parse(
            LanguageConfig languageConfig, // 语法配置
            LRTableAnalyzer lrTableAnalyzer, // lr表生成器
            LRAnalyzer lRAnalyzer, // lr表分析器
            LexicalAnalysis lexicalAnalysis, // 词法分析器
            SyntaxDirectedTranslation<T> sdt, // 语法制导
            String code,
            String tag) {
        // 1. 词法分析
        LexicalConfig lexicalConfig = LexicalConfigReader.read(languageConfig, tag);
        List<Token> tokes = lexicalAnalysis.parsing(code, lexicalConfig);
        FileUtils.writeFile("target/%s-tokens.txt".formatted(lexicalConfig.name()),
                tokes.stream().map(Objects::toString).collect(Collectors.joining("\n")));

        // 2. 语法分析
        GrammarConfig grammarConfig = GrammarReader.read(languageConfig, tag);
        grammarCheck(tokes, grammarConfig);
        LRTable lrTable = lrTableAnalyzer.analyze(grammarConfig);
        SyntaxTree syntaxTree = lRAnalyzer.analyze(lrTable, tokes);
        FileUtils.writeFile("target/%s-syntaxTree.dot".formatted(grammarConfig.name()), SyntaxTreeVisiable.toDot(syntaxTree));

        // 3. 语义分析
        return sdt.translation(syntaxTree);
    }

    private static void grammarCheck(List<Token> tokes, GrammarConfig grammarConfig) {
        List<String> symbol = grammarConfig.symbol();

        if (tokes.stream().map(Token::type).anyMatch(o -> !symbol.contains(o))) {
            throw new RuntimeException("token流 和 gramar不符");
        }
    }
}
