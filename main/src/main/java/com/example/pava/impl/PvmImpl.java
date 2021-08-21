package com.example.pava.impl;

import com.example.pava.PavaCode;
import com.example.pava.Pvm;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

/**
 * client.pava 三地址代码执行引擎
 */
public class PvmImpl implements Pvm<List<PavaDefaultThreeAddressCode>> {

    private final static Object nullObj = new Object() {
        @Override
        public String toString() {
            return "nullObj";
        }
    };
    private final Boolean debug;

    public PvmImpl(Boolean debug) {
        this.debug = debug;
    }

    @ToString
    static class Context {
        @Getter
        private Integer pc;
        @ToString.Exclude
        private final Map<String, Integer> labelPoint;
        private Map<String, Object> register;
        private final Stack<Object> stack;
        private Object returnValueRegister = nullObj;
        private final Object exitObj = new Object() {
            @Override
            public String toString() {
                return "exitObj";
            }
        };


        public Context(PavaCode<List<PavaDefaultThreeAddressCode>> pavaCode) {
            pc = 0;
            labelPoint = new HashMap<>();
            stack = new Stack<>();
            register = new HashMap<>();
            register.put(PavaDefaultThreeAddressCode.Reg.returnJumpRegister, nullObj);

            stack.push(exitObj);

            IntStream.range(0, pavaCode.pavaCode().size()).forEach(i -> {
                PavaDefaultThreeAddressCode threeAddressCode = pavaCode.pavaCode().get(i);
                if (threeAddressCode.getOperator().equals(PavaDefaultThreeAddressCode.LABEL)) {
                    Integer put = labelPoint.put(threeAddressCode.getTarget(), i);
                    if (put != null) {
                        throw new RuntimeException("错误的Pava代码，代码中多次出现label %s ".formatted(threeAddressCode.getTarget()));
                    }
                }
            });

            pc = labelPoint.get("main");
        }

        public void saveAllRegister() {
            stack.push(new HashMap<>(Map.copyOf(register)));
        }

        public void loadAllRegister() {
            register = (Map<String, Object>) stack.pop();
        }

        public Object getReg(String regName) {
            if (regName.equals(PavaDefaultThreeAddressCode.Reg.returnValueRegisterName)) {
                return returnValueRegister;
            } else {
                return register.get(regName);
            }
        }

        public void defineReg(String regName) {
            if (getReg(regName) != null) {
                throw new RuntimeException("重复定义的寄存器");
            } else {
                if (regName.equals(PavaDefaultThreeAddressCode.Reg.returnValueRegisterName)) {
                    throw new RuntimeException("不可定义Pvm内部寄存器");
                } else {
                    register.put(regName, nullObj);
                }
            }
        }

        public void undefineReg(String regName) {
            if (getReg(regName) == null) {
                throw new RuntimeException("找不到的寄存器");
            } else {
                if (regName.equals(PavaDefaultThreeAddressCode.Reg.returnValueRegisterName)) {
                    throw new RuntimeException("不可删除Pvm内部寄存器");
                } else {
                    register.remove(regName);
                }
            }
        }

        public void putReg(String regName, Object o) {
            if (getReg(regName) == null) {
                throw new RuntimeException("未定义的寄存器");
            } else {
                if (regName.equals(PavaDefaultThreeAddressCode.Reg.returnValueRegisterName)) {
                    returnValueRegister = o;
                } else {
                    register.put(regName, o);
                }
            }
        }

        public int getRegInt(String regName) {
            return (Integer) getReg(regName);
        }


        public void nextPc() {
            pc++;
        }

        public void pushStack(Object o) {
            stack.push(o);
        }


        public Object popStack() {
            return stack.pop();
        }

        public void clearReg() {
            register.clear();
            register.put(PavaDefaultThreeAddressCode.Reg.returnJumpRegister, nullObj);
        }

        public void jump(String op1) {
            pc = labelPoint.get(op1);
        }


        public void jumpReg(String target) {
            pc = labelPoint.get(register.get(target));
        }
    }

    private final Map<String, BiConsumer<PavaDefaultThreeAddressCode, Context>> map = Map.ofEntries(
            Map.entry(PavaDefaultThreeAddressCode.ADD, (code, context) -> {
                int op1 = context.getRegInt(code.getOp1());
                int op2 = context.getRegInt(code.getOp2());
                context.putReg(code.getTarget(), op1 + op2);
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.SUB, (code, context) -> {
                int op1 = context.getRegInt(code.getOp1());
                int op2 = context.getRegInt(code.getOp2());
                context.putReg(code.getTarget(), op1 - op2);
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.LT, (code, context) -> {
                int op1 = context.getRegInt(code.getOp1());
                int op2 = context.getRegInt(code.getOp2());
                context.putReg(code.getTarget(), op1 < op2 ? 1 : 0);
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.EQUAL, (code, context) -> {
                int op1 = context.getRegInt(code.getOp1());
                int op2 = context.getRegInt(code.getOp2());
                context.putReg(code.getTarget(), op1 == op2 ? 1 : 0);
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN_NUMBER, (code, context) -> {
                context.putReg(code.getTarget(), Integer.parseInt(code.getOp1()));
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN, (code, context) -> {
                int op1 = context.getRegInt(code.getOp1());
                context.putReg(code.getTarget(), op1);
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.LABEL, (code, context) -> {
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.LOAD_FROM_STACK, (code, context) -> {
                context.putReg(code.getTarget(), context.popStack());
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.SAVE_TO_STACK, (code, context) -> {
                context.pushStack(context.getReg(code.getTarget()));
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.DEFINE_REG, (code, context) -> {
                context.defineReg(code.getTarget());
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.SAVE_ALL_REG_TO_STACK, (code, context) -> {
                context.saveAllRegister();
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.ASSIGN_STRING, (code, context) -> {
                context.putReg(code.getTarget(), code.getOp1());
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.CLEAR_REG, (code, context) -> {
                context.clearReg();
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.JUMP, (code, context) -> {
                context.jump(code.getTarget());
            }),
            Map.entry(PavaDefaultThreeAddressCode.JFALSE, (code, context) -> {
                if (context.getRegInt(code.getOp1()) == 0) {
                    context.jump(code.getTarget());
                } else {
                    context.nextPc();
                }
            }),
            Map.entry(PavaDefaultThreeAddressCode.UNDEFINE_SYMBOL, (code, context) -> {
                context.undefineReg(code.getTarget());
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.JUMP_REG, (code, context) -> {
                if (context.getReg(code.getTarget()) == context.exitObj) {
                    context.pc = -1;
                } else {
                    context.jumpReg(code.getTarget());
                }
            }),
            Map.entry(PavaDefaultThreeAddressCode.LOAD_ALL_REG_FROM_STACK, (code, context) -> {
                context.loadAllRegister();
                context.nextPc();
            }),
            Map.entry(PavaDefaultThreeAddressCode.UPDATE, (code, context) -> {
                context.putReg(code.getTarget(), context.getReg(code.getOp1()));
                context.nextPc();
            })
    );

    @Override
    public int run(PavaCode<List<PavaDefaultThreeAddressCode>> pavaCode, String[] args) {
        // 初始化pava程序上下文
        Context context = new Context(pavaCode);

        // 运行pava代码
        List<PavaDefaultThreeAddressCode> pavaDefaultThreeAddressCodes = pavaCode.pavaCode();
        while (context.getPc() != -1 && context.getPc() < pavaDefaultThreeAddressCodes.size()) {
            PavaDefaultThreeAddressCode threeAddressCode = pavaDefaultThreeAddressCodes.get(context.getPc());
            if (debug) {
                System.out.print("current code : " + threeAddressCode + ", ");
            }

            BiConsumer<PavaDefaultThreeAddressCode, Context> consumer = map.get(threeAddressCode.getOperator());
            if (consumer == null) {
                throw new RuntimeException("无法解析三地址代码：" + threeAddressCode);
            }
            consumer.accept(threeAddressCode, context);
            if (debug) {
                System.out.println("context: " + context);
            }

        }
        return context.getRegInt(PavaDefaultThreeAddressCode.Reg.returnValueRegisterName);
    }
}
