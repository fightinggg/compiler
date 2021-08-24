package com.example.sdt;

import com.example.syntaxtree.SyntaxTree;

public interface SyntaxDirectedTranslation<T> {
    T translation(SyntaxTree syntaxTree);
}
