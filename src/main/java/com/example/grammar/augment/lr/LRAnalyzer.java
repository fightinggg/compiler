package com.example.grammar.augment.lr;

import com.example.syntaxtree.SyntaxTree;

public interface LRAnalyzer {
    SyntaxTree analyze(LRTable lrTable);
}
