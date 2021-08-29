package com.example.lang.cpp;

import com.example.config.LanguageConfig;
import com.example.config.Reader;
import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.lang.Lang;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.pava.impl.PavaDefaultThreeAddressCode;
import com.example.syntaxtree.SyntaxTree;
import com.example.visiable.FileUtils;
import com.example.visiable.SyntaxTreeVisiable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cpp {
    public static List<PavaDefaultThreeAddressCode> parse(String code, String tag) {
        if (System.getenv("PAVA_HOME") == null) {
            throw new RuntimeException("请在环境变量中配置PAVA_HOME");
        }
        List<PavaDefaultThreeAddressCode> pavaDefaultThreeAddressCodes = Lang.parse(
                Reader.read(System.getenv("PAVA_HOME") + "/config/cpp.json"),
                new LR1TableAnalyzer(),
                new LRAnalyzerImpl(),
                new LexicalAnalysisImpl(),
                new CppSyntaxDirectedTranslation(),
                code,
                tag
        );
        FileUtils.writeFile("target/%s-threeAddressCodes.txt".formatted(tag),
                pavaDefaultThreeAddressCodes.stream().map(Object::toString).collect(Collectors.joining("\n")));
        return pavaDefaultThreeAddressCodes;
    }
}
