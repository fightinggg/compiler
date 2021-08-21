package com.example.grammar.augment.lr;

import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;

import java.util.List;

public interface LRAnalyzer {
    SyntaxTree analyze(LRTable lrTable, List<Token> tokenList);
}
