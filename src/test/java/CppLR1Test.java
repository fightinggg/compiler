import com.example.lang.cpp.Cpp;
import com.example.pava.impl.PavaDefaultThreeAddressCode;
import org.junit.Test;

import java.util.List;

// https://dreampuf.github.io/GraphvizOnline/

public class CppLR1Test {

    @Test
    public void allTest() {
        String code = """
                                
                void ifFunc(int x){
                    if(x==1){
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
                    int a = 10;
                    int b = 2;
                    int d = a + b;
                    int e = d + 0;
                    int fib7 = fib(5) + fib(6);
                    if ( a+b ){
                         a = 1;
                    }
                     while( a+b){
                          d = 2;
                          c = 1;
                          a = a - 1;
                          b = 0;
                     }
                     for(int i=0;i<10;i=i+1){
                         s = s + i;
                     }
                     
                     for(int i=0;i<20;i=i+1) {
                        int x = 3;
                        while(x){
                            b = c + 1;
                            x = x -1;
                        }
                        a = a+1;
                     }
                     return fib7;
                }

                """;

        Cpp.parse(code, "CppLR1Test.allTest");
    }

    @Test
    public void mulOrDelOrModExpressionSeqtest() {
        String code = """
                int main(){
                    int a = 1 * c * 1 / 2 % 1 *2 %1;
                }
                """;
        List<PavaDefaultThreeAddressCode> parse = Cpp.parse(code, "CppLR1Test.mulOrDelOrModExpressionSeqtest");

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
                    if(x==1){
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
                    if(x==1+2*3){
                        int y = 1;
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
                int fib(int x){
                    if(x<2) return 1;
                    return fib(x-1) + fib(x-2);
                }
                
                int main(){
                    return fib(5);
                }
                """;
        Cpp.parse(code, "CppLR1Test.functionInvokerTest4");
    }


}
