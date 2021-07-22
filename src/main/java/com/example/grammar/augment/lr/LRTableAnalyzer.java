package com.example.grammar.augment.lr;

import com.example.grammar.GrammarConfig;
import com.example.lexical.Token;

import java.util.List;

public interface LRTableAnalyzer {
    LRTable analyze(GrammarConfig grammarConfig);
}
