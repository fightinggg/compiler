package com.example.grammar.augment.lr;

import com.example.grammar.Production;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

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
        stack.push(new StackNode(begin, null));

        for (int i = 0; i < tokenList.size(); i++) {
            Token token = tokenList.get(i);

            final Integer tokenId = lrTable.getSymbolId().get(token.type());
            LRTable.Action action = lrTable.getActionTable()[stack.peek().getState()][tokenId];
            if (action == null) {
                throw new RuntimeException("could not analtze %s".formatted(tokenList.toString()));
            }
            if (action.getAc().equals("s")) {
                stack.push(new StackNode(action.getJump(), new SyntaxTree.Node(token, null, null)));
                // System.out.println(stack + " read " + token.raw() + " push " + action.getJump());
            } else if (action.getAc().equals("r")) {
                Production production = lrTable.getProductionArray()[action.getJump()];
                List<SyntaxTree.Node> pops = new ArrayList<>();
                production.rightSymbol().forEach(o -> pops.add(stack.pop().getNode()));
                Collections.reverse(pops);
                Integer[] currentGoto = lrTable.getGotoTable()[stack.peek().getState()];

                stack.push(new StackNode(currentGoto[lrTable.getSymbolId().get(production.leftSymbol())], new SyntaxTree.Node(token, production, pops)));
                //System.out.println(stack + " find " + token.raw() + "pop some and push "
                //        + currentGoto.get(production.leftSymbol()));
                //System.out.println(production);

                i--;
            } else if (action.getAc().equals(LRTable.Action.ACC)) {
                //System.out.println("wow parse success!!");
            } else {
                throw new RuntimeException();
            }
        }

        SyntaxTree syntaxTree = new SyntaxTree();
        syntaxTree.setRoot(stack.peek().getNode());
        return syntaxTree;
    }
}
