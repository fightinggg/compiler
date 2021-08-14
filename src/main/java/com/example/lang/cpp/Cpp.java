package com.example.lang.cpp;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;
import com.example.visiable.FileUtils;
import com.example.visiable.SyntaxTreeVisiable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cpp {
    public static List<CppSyntaxDirectedTranslation.ThreeAddressCode> parse(String code, String tag) {
        String propertiesFile = "cpp.json";
        LRTableAnalyzer lrTableAnalyzer = new LR1TableAnalyzer();

        // 1. 词法分析
        LexicalConfig lexicalConfig = LexicalConfigReader.read(propertiesFile, tag);
        List<Token> tokes = new LexicalAnalysisImpl().parsing(code, lexicalConfig);
        FileUtils.writeFile("target/%s-tokens.txt".formatted(lexicalConfig.name()),
                tokes.stream().map(Objects::toString).collect(Collectors.joining("\n")));

        // 2. 语法分析
        GrammarConfig grammarConfig = GrammarReader.read(propertiesFile, tag);
        LRTable lrTable = lrTableAnalyzer.analyze(grammarConfig);
        SyntaxTree syntaxTree = new LRAnalyzerImpl().analyze(lrTable, tokes);
        FileUtils.writeFile("target/%s-syntaxTree.dot".formatted(grammarConfig.name()), SyntaxTreeVisiable.toDot(syntaxTree));

        // 3. 语义分析
        List<CppSyntaxDirectedTranslation.ThreeAddressCode> threeAddressCodes = CppSyntaxDirectedTranslation.translation(syntaxTree).stream().toList();
        FileUtils.writeFile("target/%s-threeAddressCodes.txt".formatted(grammarConfig.name()),
                threeAddressCodes.stream().map(Object::toString).collect(Collectors.joining("\n")));
        return threeAddressCodes;
    }
}
