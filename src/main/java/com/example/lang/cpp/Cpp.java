package com.example.lang.cpp;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lang.reg.RegLexicalAnalysisImpl;
import com.example.lang.reg.RegSyntaxDirectedTranslation;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.nfa.Nfa;
import com.example.nfa.NfaImpl;
import com.example.syntaxtree.SyntaxTree;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cpp {
    public static Nfa<Object, String> parse(String code) {
        // 1. 获取词法和文法配置
        LexicalConfig lexicalConfig = LexicalConfigReader.read("cpp.json");
        GrammarConfig grammarConfig = GrammarReader.read("cpp.json");

        // 2. 根据文法构建SLR语法分析器
        LRTable lrTable = new SLRTableAnalyzer().analyze(grammarConfig);

        // 3. 执行词法分析
        List<Token> tokes = new LexicalAnalysisImpl().parsing(code, lexicalConfig);

        return null;
    }
}
