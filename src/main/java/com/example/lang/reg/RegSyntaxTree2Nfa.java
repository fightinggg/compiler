package com.example.lang.reg;

import com.example.lexical.Token;
import com.example.nfa.Nfa;
import com.example.syntaxtree.SyntaxTree;

public class RegSyntaxTree2Nfa {

    public static Nfa<Object, Token> toNfa(SyntaxTree.Node node) {
        if (node.getSon() != null) {

        } else {

        }
    }
}
