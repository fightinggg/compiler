import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;
import com.example.visiable.FileUtils;
import com.example.visiable.SyntaxTreeVisiable;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IfElseAmbiguousLR1Test {

    @Test
    public void ifElseTest() {
        String code = """
                if ( condition ) notIfBlockUnit 
                else notIfBlockUnit
                """;

        String tag = "IfElseAmbiguousLR1Test.ifElseTest";
        test(code, tag);
    }

    @Test
    public void ifElseTest2() {
        String code = """
                if ( condition ) 
                    if ( condition ) 
                        if ( condition ) 
                            if ( condition ) notIfBlockUnit 
                            else notIfBlockUnit 
                        else notIfBlockUnit 
                    else notIfBlockUnit 
                else notIfBlockUnit
                """;

        String tag = "IfElseAmbiguousLR1Test.ifElseTest2";
        test(code, tag);
    }


    @Test
    public void ifElseTest3() {
        String code = """
                if ( condition ) 
                    if ( condition ) 
                        if ( condition ) 
                            if ( condition ) notIfBlockUnit 
                            else notIfBlockUnit
                """;

        String tag = "IfElseAmbiguousLR1Test.ifElseTest3";
        test(code, tag);
    }


    private void test(String code, String tag) {
        // 1. 获取词法和文法配置
        LexicalConfig lexicalConfig = LexicalConfigReader.read("ifelseAmbiguous.json", tag);
        GrammarConfig grammarConfig = GrammarReader.read("ifelseAmbiguous.json", tag);

        // 2. 执行词法分析
        List<Token> tokes = new LexicalAnalysisImpl().parsing(code, lexicalConfig);
        FileUtils.writeFile("target/%s-tokens.txt".formatted(grammarConfig.name()),
                tokes.stream().map(Objects::toString).collect(Collectors.joining("\n")));


        // 3. 根据文法构建SLR语法分析器
        LRTable lrTable = new LR1TableAnalyzer().analyze(grammarConfig);

        // 4. 执行语法分析
        final SyntaxTree syntaxTree = new LRAnalyzerImpl().analyze(lrTable, tokes);

        final String syntaxTreeString = SyntaxTreeVisiable.toDot(syntaxTree);
        FileUtils.writeFile("target/%s-syntaxTree.dot".formatted(grammarConfig.name()), syntaxTreeString);

    }

}
