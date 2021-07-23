package com.example.grammar.augment.lr;

import com.example.grammar.GrammarFollowSet;
import com.example.grammar.Production;
import com.example.grammar.ProductionImpl;
import com.example.grammar.augment.lr.slr.SLRAugmentProduction;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LRAnalyzerImpl implements LRAnalyzer {

    @Override
    public SyntaxTree analyze(LRTable lrTable, List<Token> tokenList) {
        @Getter
        @AllArgsConstructor
        class StackNode {
            private final Integer state;
            private final SyntaxTree.Node node;
        }
        Stack<StackNode> stack = new Stack<>();
        int begin = lrTable.getBegin();
        stack.push(new StackNode(begin, new SyntaxTree.Node("", new ProductionImpl("null", new ArrayList<>()), new ArrayList<>())));

        for (int i = 0; i - 1 < tokenList.size(); i++) {
            String type;
            String raw;
            if (i == tokenList.size()) {
                type = GrammarFollowSet.FOLLOW_END;
                raw = GrammarFollowSet.FOLLOW_END;
            } else {
                Token token = tokenList.get(i);
                type = token.type();
                raw = token.raw();
            }
            Map<String, LRTable.Action> currentActionTable = lrTable.getActionTable().get(stack.peek().getState());
            LRTable.Action action = currentActionTable.get(type);
            if (action.getAc().equals("s")) {
                stack.push(new StackNode(action.getJump(), new SyntaxTree.Node(raw, new ProductionImpl("", new ArrayList<>()), new ArrayList<>())));
                System.out.println(stack + " read " + raw + " push " + action.getJump());
            } else if (action.getAc().equals("r")) {
                Production production = lrTable.getProductions().get(action.getJump());
                List<SyntaxTree.Node> pops = new ArrayList<>();
                production.rightSymbol().forEach(o -> pops.add(stack.pop().getNode()));
                Map<String, Integer> currentGoto = lrTable.getGotoTable().get(stack.peek().getState());
                stack.push(new StackNode(currentGoto.get(production.leftSymbol()), new SyntaxTree.Node("", production, pops)));
                System.out.println(stack + " find " + raw + "pop some and push "
                        + currentGoto.get(production.leftSymbol()));
                System.out.println(production);

                i--;
            } else if (action.getAc().equals(LRTable.Action.ACC)) {
                System.out.println("wow parse success!!");
            } else {
                throw new RuntimeException();
            }
        }

        SyntaxTree syntaxTree = new SyntaxTree();
        syntaxTree.setRoot(stack.peek().getNode());
        return syntaxTree;
    }
}
