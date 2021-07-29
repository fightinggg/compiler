package com.example.grammar.augment.lr;

import com.example.grammar.augment.lr.slr.SLRAugmentProduction;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;

import java.util.List;
import java.util.Set;

public interface LRAnalyzer {
    SyntaxTree analyze(LRTable lrTable, List<Token> tokenList);
}
