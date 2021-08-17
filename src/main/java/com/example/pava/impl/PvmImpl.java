package com.example.pava.impl;

import com.example.pava.PavaCode;
import com.example.pava.Pvm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

/**
 * pava 三地址代码执行引擎
 */
public class PvmImpl implements Pvm<List<PavaDefaultThreeAddressCode>> {

    @Data
    static class Context {
        private Integer pc;
        private Map<String, Integer> labelPoint;
        private Map<String, Integer> intMemory;
        private Stack<Integer> stack;

        public Context(PavaCode<List<PavaDefaultThreeAddressCode>> pavaCode) {
            pc = 0;
            intMemory = new HashMap<>();
            labelPoint = new HashMap<>();
            stack = new Stack<>();

            IntStream.range(0, pavaCode.pavaCode().size()).forEach(i -> {
                PavaDefaultThreeAddressCode threeAddressCode = pavaCode.pavaCode().get(i);
                if (threeAddressCode.getOperator().equals(PavaDefaultThreeAddressCode.LABEL)) {
                    labelPoint.put(threeAddressCode.getTarget(), i);
                }
            });

        }
    }

    private final Map<String, BiConsumer<PavaDefaultThreeAddressCode, Context>> map = Map.ofEntries(
            Map.entry(PavaDefaultThreeAddressCode.ADD, (code, context) -> {
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN, (code, context) -> {
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, (code, context) -> {
            }),
            Map.entry(PavaDefaultThreeAddressCode.LABEL, (code, context) -> {
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.PARMA_LOAD, (code, context) -> {
                context.getIntMemory().put(code.getTarget(), context.getStack().pop());
            })
    );

    @Override
    public int run(PavaCode<List<PavaDefaultThreeAddressCode>> pavaCode, String[] args) {
        // 初始化pava程序上下文
        Context context = new Context(pavaCode);
        context.getStack().push(1);
        context.getStack().push(1);
        context.getStack().push(1);

        // 运行pava代码
        List<PavaDefaultThreeAddressCode> pavaDefaultThreeAddressCodes = pavaCode.pavaCode();
        while (true) {
            PavaDefaultThreeAddressCode threeAddressCode = pavaDefaultThreeAddressCodes.get(context.getPc());
            BiConsumer<PavaDefaultThreeAddressCode, Context> consumer = map.get(threeAddressCode.getOperator());
            if (consumer == null) {
                throw new RuntimeException("无法解析三地址代码：" + threeAddressCode);
            }
            consumer.accept(threeAddressCode, context);
        }
    }
}
