package com.example.lang.reg;

import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.nfa.Nfa;
import com.example.syntaxtree.SyntaxTree;

import java.util.List;

public class Reg {
    public static Nfa<Object, String> parse(String code) {
        // 1. 获取词法和文法配置
        LexicalConfig lexicalConfig = LexicalConfigReader.read("reg.json", "");
        GrammarConfig grammarConfig = GrammarReader.read("reg.json", "");

        // 2. 根据文法构建SLR语法分析器
        LRTable lrTable = new SLRTableAnalyzer().analyze(grammarConfig);

        // 3. 执行词法分析
        List<Token> tokes = new RegLexicalAnalysisImpl().parsing(code, lexicalConfig);

        // 4. 执行语法分析
        SyntaxTree syntaxTree = new LRAnalyzerImpl().analyze(lrTable, tokes);

        // 5. 执行语法制导翻译
        return RegSyntaxDirectedTranslation.toNfa(syntaxTree);
    }
}
