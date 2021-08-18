package com.example.pava.impl;

import com.example.pava.PavaCode;
import com.example.pava.Pvm;
import lombok.Data;
import lombok.ToString;

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
        @ToString.Exclude
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

            pc = labelPoint.get("main");
        }
    }

    private final Map<String, BiConsumer<PavaDefaultThreeAddressCode, Context>> map = Map.ofEntries(
            Map.entry(PavaDefaultThreeAddressCode.ADD, (code, context) -> {
                int op1 = context.getIntMemory().get(code.getOp1());
                int op2 = context.getIntMemory().get(code.getOp2());
                context.getIntMemory().put(code.getTarget(), op1 + op2);
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.EQUAL, (code, context) -> {
                int op1 = context.getIntMemory().get(code.getOp1());
                int op2 = context.getIntMemory().get(code.getOp2());
                context.getIntMemory().put(code.getTarget(), op1 == op2 ? 1 : 0);
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, (code, context) -> {
                context.getIntMemory().put(code.getTarget(), Integer.parseInt(code.getOp1()));
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN, (code, context) -> {
                int op1 = context.getIntMemory().get(code.getOp1());
                context.getIntMemory().put(code.getTarget(), op1);
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.LABEL, (code, context) -> {
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.PARMA_LOAD, (code, context) -> {
                if (context.getStack().empty()) {
                    throw new RuntimeException("虚拟机内部错误, pava栈空");
                }
                context.getIntMemory().put(code.getTarget(), context.getStack().pop());
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.PARMA_PUT, (code, context) -> {
                context.getStack().push(context.getIntMemory().get(code.getTarget()));
                context.setPc(context.getPc() + 1);
            }),
            Map.entry(PavaDefaultThreeAddressCode.CALL, (code, context) -> {
                String returnAddress = code.getTarget();
                String functionName = code.getOp1();
                // 放入返回地址
                context.getStack().push(context.getIntMemory().get(code.getTarget()));
                context.setPc(context.getLabelPoint().get(functionName));
                throw new RuntimeException("todo");
            })
    );

    @Override
    public int run(PavaCode<List<PavaDefaultThreeAddressCode>> pavaCode, String[] args) {
        // 初始化pava程序上下文
        Context context = new Context(pavaCode);

        // 运行pava代码
        List<PavaDefaultThreeAddressCode> pavaDefaultThreeAddressCodes = pavaCode.pavaCode();
        while (true) {
            PavaDefaultThreeAddressCode threeAddressCode = pavaDefaultThreeAddressCodes.get(context.getPc());
            System.out.print("tac: " + threeAddressCode + ", ");

            BiConsumer<PavaDefaultThreeAddressCode, Context> consumer = map.get(threeAddressCode.getOperator());
            if (consumer == null) {
                throw new RuntimeException("无法解析三地址代码：" + threeAddressCode);
            }
            consumer.accept(threeAddressCode, context);
            System.out.println("context: " + context);
        }
    }
}
