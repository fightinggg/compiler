package syntaxtree.visiable;

import syntaxtree.SyntaxTree;


public class SyntaxTreeVisiable {

    private static int dfs(SyntaxTree.Node node, Integer[] id, StringBuilder stringBuilder) {
        int res = id[0]++;


        if (node.getSon() != null) {
            String raw = node.getProduction().getFrom() + " => " + String.join(" ", node.getProduction().getDerive());
            stringBuilder.append(res).append("[ label=\"").append(raw).append("\"]").append("\n");
            for (SyntaxTree.Node son : node.getSon()) {
                int sonid = dfs(son, id, stringBuilder);
                stringBuilder.append(res).append(" -> ").append(sonid).append("\n");
            }
        } else {
            String raw = node.getRaw();
            stringBuilder.append(res).append("[ label=\"").append(raw).append("\"]").append("\n");
        }
        return res;
    }


    public static String toDot(SyntaxTree syntaxTree) {
        Integer[] integers = new Integer[1];
        integers[0] = 0;
        StringBuilder stringBuilder = new StringBuilder();
        dfs(syntaxTree.getRoot(), integers, stringBuilder);
        return "digraph { \n" + stringBuilder + "}";
    }
}
