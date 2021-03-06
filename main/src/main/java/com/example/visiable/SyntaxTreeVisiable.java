package com.example.visiable;


import com.example.syntaxtree.SyntaxTree;
import com.example.utils.ToStringUtils;

public class SyntaxTreeVisiable {

    private static int dfs(SyntaxTree.Node node, Integer[] id, StringBuilder stringBuilder) {
        int res = id[0]++;


        if (node.getSon() != null) {
            String nodeString = "%d[ label = \"%s\" ] \n ".formatted(res, node.getProduction().raw());

            stringBuilder.append(nodeString);
            for (SyntaxTree.Node son : node.getSon()) {
                int sonid = dfs(son, id, stringBuilder);
                stringBuilder.append("%d -> %d \n".formatted(res, sonid));
            }
        } else {
            String raw = node.getToken().raw();
            stringBuilder.append("%d[ label = %s]\n".formatted(res, ToStringUtils.toString(raw)));
        }
        return res;
    }


    public static String toDot(SyntaxTree syntaxTree) {
        Integer[] integers = new Integer[1];
        integers[0] = 0;
        StringBuilder stringBuilder = new StringBuilder();
        dfs(syntaxTree.getRoot(), integers, stringBuilder);
        return "digraph { \n%s}".formatted(stringBuilder);
    }
}
