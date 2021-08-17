package com.example.pava.impl;

import com.example.pava.PavaCode;
import com.example.pava.Pvm;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class PvmImpl implements Pvm<List<PavaDefaultThreeAddressCode>> {

    static class Context {

    }

   private Map<String, BiConsumer<PavaDefaultThreeAddressCode, Context>> map = Map.ofEntries(
            Map.entry(PavaDefaultThreeAddressCode.ADD, (code, context) -> {
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN, (code, context) -> {
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, (code, context) -> {
            })
    );

    @Override
    public int run(PavaCode<List<PavaDefaultThreeAddressCode>> pavaCode, String[] args) {
        // 初始化pava程序上下文
        Context context = new Context();

        // 运行pava代码
        List<PavaDefaultThreeAddressCode> pavaDefaultThreeAddressCodes = pavaCode.pavaCode();
        for (PavaDefaultThreeAddressCode threeAddressCode : pavaDefaultThreeAddressCodes) {
            map.get(threeAddressCode.getOperator()).accept(threeAddressCode, context);
        }
        return 0;
    }
}
