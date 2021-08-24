package com.example.pava.impl;

import com.example.lang.cpp.Cpp;
import org.junit.Test;

import java.util.stream.Collectors;

public class PvmImplTest {
    @Test
    public void functionCode() {
        String code = """
                int fib(int x){
                    if(x<2) return 1;
                    return fib(x-1) + fib(x-2);
                }
                                
                int main(){
                    int a = fib(5);
                    return a;
                }
                """;

        test(code, "CppLR1Test.functionData");
    }

    @Test
    public void forCode() {
        String code = """
                int main(){
                    int a1 = 1;
                    int a2 = 1;
                    for(int i=2;i<10;i=i+1){
                        int a3 = a1+a2;
                        a1=a2;
                        a2=a3;
                    }
                    return a2;
                }
                """;
        test(code, "CppLR1Test.forData");
    }

    @Test
    public void whileCode() {
        String code = """
                int main(){
                    int a1 = 1;
                    int a2 = 1;
                    int i = 2;
                    while(i<10){
                        i = i +1;
                        int sum = a1 + a2;
                        a1 = a2;
                        a2 = sum;
                    }
                    return a2;
                }
                """;
        test(code, "CppLR1Test.whileCode");
    }


    private void test(String code, String tag) {
        code = Cpp.parse(code, tag).stream().map(Object::toString).collect(Collectors.joining("\n"));
        System.out.println("code: \n" + code);

        PavaCodeImpl pavaCode = new PavaCodeImpl(code);

        System.out.println(new PvmImpl(true).run(pavaCode, null));
    }
}


