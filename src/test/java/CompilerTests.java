import grammar.NormalGrammar;
import grammar.GrammarOptimizer;
import grammar.JsonGrammarReader;
import org.junit.Test;
import syntaxtree.SyntaxTree;
import syntaxtree.visiable.SyntaxTreeVisiable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CompilerTests {

    String cppCode = "    int a = 1;\n" +
            "    int a = 1;\n" +
            "    int a = 1;\n" +
            "\n";

    @Test
    public void DfsParsingTest() {
        NormalGrammar normalGrammar = JsonGrammarReader.read("cpp-lex.json");
        GrammarOptimizer.optimizer(normalGrammar);
        //        System.out.println(grammar);
        SyntaxTree syntaxTree = new DfsParsing().parsing(cppCode, normalGrammar);
        String dot = SyntaxTreeVisiable.toDot(syntaxTree);
        try (OutputStream outputStream = new FileOutputStream("target/dot.txt")) {
            outputStream.write(dot.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
