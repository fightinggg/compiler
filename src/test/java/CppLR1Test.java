import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.lr1.LR1TableAnalyzer;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lang.cpp.Cpp;
import com.example.lang.cpp.CppSyntaxDirectedTranslation;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.sdt.SyntaxDirectedTranslation;
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
                                
                void ifFunc(int x){
                    if(a==1){
                        return 1;
                    } else {
                        return 2;
                    }
                }
                                
                int fib(int x){
                    if(x<2) return 1;
                    return fib(x-1) + fib(x-2);
                }
                                
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

        Cpp.parse(code, "allTest");
    }

    @Test
    public void mulOrDelOrModExpressionSeqtest() {
        String code = """
                int main(){
                    int a = 1 * c * 1 / 2 % 1 *2 %1;
                }
                """;
        List<CppSyntaxDirectedTranslation.ThreeAddressCode> parse = Cpp.parse(code, "CppLR1Test.mulOrDelOrModExpressionSeqtest");

    }

    @Test
    public void addOrSubExpressionSeqtest() {
        String code = """
                int main(){
                    int a = 1*2+3*4*4+1-2/2-3%3;
                }
                """;
        Cpp.parse(code, "CppLR1Test.addOrSubExpressionSeqtest");
    }


    @Test
    public void expressionSeqtest() {
        String code = """
                int main(){
                    int a = (1*2+3*4*4+1-2/2-3%3)+1*(2+4)*1*2%3/(3+1);
                }
                """;
        Cpp.parse(code, "CppLR1Test.expressionSeqtest");
    }


    @Test
    public void ifTest() {
        String code = """
                void ifFunc(int x){
                    if(a==1){
                        return 1;
                    }
                }
                """;
        Cpp.parse(code, "CppLR1Test.ifTest");
    }

    @Test
    public void ifElseTest() {
        String code = """
                void ifFunc(int x){
                    if(a==1){
                        return 1;
                    } else {
                        return 2;
                    }
                }
                """;
        Cpp.parse(code, "CppLR1Test.ifElseTest");
    }


    @Test
    public void whileTest() {
        String code = """
                void ifFunc(int x){
                    while(a==1){
                        return 1;
                    }
                }
                """;
        Cpp.parse(code, "CppLR1Test.whileTest");
    }

    @Test
    public void functionInvokerTest1() {
        String code = """
                int fib(){
                    return fib();
                }
                """;
        Cpp.parse(code, "CppLR1Test.functionInvokerTest1");
    }

    @Test
    public void functionInvokerTest2() {
        String code = """
                int fib(int x){
                    return fib(x-1);
                }
                """;
        Cpp.parse(code, "CppLR1Test.functionInvokerTest2");
    }

    @Test
    public void functionInvokerTest3() {
        String code = """
                int fib(int x,int y){
                    return fib(x-1,y+1);
                }
                """;
        Cpp.parse(code, "CppLR1Test.functionInvokerTest3");
    }


    @Test
    public void functionInvokerTest4() {
        String code = """
                int fib(int x,int y){
                    return fib(x-1,y+1) + fib(1,2) + fib(3,y-3);
                }
                """;
        Cpp.parse(code, "CppLR1Test.functionInvokerTest4");
    }


}
