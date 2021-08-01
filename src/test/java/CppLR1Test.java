import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
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

// https://dreampuf.github.io/GraphvizOnline/

public class CppLR1Test {

    @Test
    public void allTest() {
        String code = """
                int main(){
                    int a = 1;
                    int d = a + b;
                    int e = d + 0;
                    String s = "abc";
                    int invoke = f1() + f2(1);
                    if ( a+b ){
                         a = 1;
                     }
                     while( a+b){
                          d = 2;
                          c = 1;
                     }
                     for(int i=0;i;i=i+1){
                         s = s + i;
                     }
                     
                     for(int i=0;i;i=i+1) {
                        while(i){
                            b = c + 1;
                        }
                        a = a+1;
                     }
                }
                """;

        test(code, "");
    }

    @Test
    public void mulOrDelOrModExpressionSeqtest() {
        String code = """
                int main(){
                    int a = 1 * c * 1 / 2 % 1 *2 %1;
                }
                """;
        test(code, "mulOrDelOrModExpressionSeqtest");
    }


    @Test
    public void addOrSubExpressionSeqtest() {
        String code = """
                int main(){
                    int a = 1*2+3*4*4+1-2/2-3%3;
                }
                """;
        test(code, "addOrSubExpressionSeqtest");
    }

    private void test(String code, String tag) {
        // 1. 获取词法和文法配置
        LexicalConfig lexicalConfig = LexicalConfigReader.read("cpp.json", tag);
        GrammarConfig grammarConfig = GrammarReader.read("cpp.json", tag);

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
