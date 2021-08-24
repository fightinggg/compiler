package com.example.lang.reg;

import com.example.config.LanguageConfig;
import com.example.config.Reader;
import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lang.Lang;
import com.example.lang.cpp.CppSyntaxDirectedTranslation;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.nfa.Nfa;
import com.example.pava.impl.PavaDefaultThreeAddressCode;
import com.example.syntaxtree.SyntaxTree;
import com.example.visiable.FileUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Reg {
    public static Nfa<Object, String> parse(String code, String tag) {
        return Lang.parse(
                Reader.read(System.getenv("PAVA_HOME") + "/config/reg.json"),
                new SLRTableAnalyzer(),
                new LRAnalyzerImpl(),
                new RegLexicalAnalysisImpl(),
                new RegSyntaxDirectedTranslation(),
                code,
                tag
        );

    }
}
