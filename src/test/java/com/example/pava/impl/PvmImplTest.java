package com.example.pava.impl;

import org.junit.Test;

public class PvmImplTest {
    @Test
    public void run() {
        String code = Config.data;

        PavaCodeImpl pavaCode = new PavaCodeImpl(code);

        new PvmImpl().run(pavaCode, null);
    }

    class Config {
        static String data = """
                label                fib                  null                 null               \s
                defineSymbol         saveReg_2            null                 null               \s
                defineSymbol         saveReg_1            null                 null               \s
                defineSymbol         saveReg_3            null                 null               \s
                defineSymbol         saveReg_15           null                 null               \s
                defineSymbol         saveReg_6            null                 null               \s
                defineSymbol         saveReg_5            null                 null               \s
                defineSymbol         saveReg_7            null                 null               \s
                defineSymbol         saveReg_9            null                 null               \s
                defineSymbol         saveReg_11           null                 null               \s
                defineSymbol         saveReg_10           null                 null               \s
                defineSymbol         saveReg_12           null                 null               \s
                defineSymbol         saveReg_14           null                 null               \s
                defineSymbol         x                    null                 null               \s
                loadFromStack        saveReg_returnJump   null                 null               \s
                loadFromStack        x                    null                 null               \s
                assignNumber         saveReg_1            2                    null               \s
                lt                   saveReg_2            x                    saveReg_1          \s
                jFalse               returnLabel_4        saveReg_2            null               \s
                assignNumber         saveReg_3            1                    null               \s
                assign               pvmReg_returnValue   saveReg_3            null               \s
                jump                 returnLabel_0        null                 null               \s
                label                returnLabel_4        null                 null               \s
                assignNumber         saveReg_5            1                    null               \s
                sub                  saveReg_6            x                    saveReg_5          \s
                saveAllRegToStack    null                 null                 null               \s
                saveToStack          saveReg_6            null                 null               \s
                assignString         saveReg_9            label_8              null               \s
                saveToStack          saveReg_9            null                 null               \s
                clearReg             fib                  null                 null               \s
                jump                 fib                  null                 null               \s
                label                label_8              null                 null               \s
                loadAllFromStack     null                 null                 null               \s
                assign               saveReg_7            pvmReg_returnValue   null               \s
                assignNumber         saveReg_10           2                    null               \s
                sub                  saveReg_11           x                    saveReg_10         \s
                saveAllRegToStack    null                 null                 null               \s
                saveToStack          saveReg_11           null                 null               \s
                assignString         saveReg_14           label_13             null               \s
                saveToStack          saveReg_14           null                 null               \s
                clearReg             fib                  null                 null               \s
                jump                 fib                  null                 null               \s
                label                label_13             null                 null               \s
                loadAllFromStack     null                 null                 null               \s
                assign               saveReg_12           pvmReg_returnValue   null               \s
                add                  saveReg_15           saveReg_7            saveReg_12         \s
                assign               pvmReg_returnValue   saveReg_15           null               \s
                jump                 returnLabel_0        null                 null               \s
                label                returnLabel_0        null                 null               \s
                undefineSymbol       saveReg_2            null                 null               \s
                undefineSymbol       saveReg_1            null                 null               \s
                undefineSymbol       saveReg_3            null                 null               \s
                undefineSymbol       saveReg_15           null                 null               \s
                undefineSymbol       saveReg_6            null                 null               \s
                undefineSymbol       saveReg_5            null                 null               \s
                undefineSymbol       saveReg_7            null                 null               \s
                undefineSymbol       saveReg_9            null                 null               \s
                undefineSymbol       saveReg_11           null                 null               \s
                undefineSymbol       saveReg_10           null                 null               \s
                undefineSymbol       saveReg_12           null                 null               \s
                undefineSymbol       saveReg_14           null                 null               \s
                undefineSymbol       x                    null                 null               \s
                jumpReg              saveReg_returnJump   null                 null               \s
                label                main                 null                 null               \s
                defineSymbol         saveReg_17           null                 null               \s
                defineSymbol         saveReg_18           null                 null               \s
                defineSymbol         saveReg_20           null                 null               \s
                assignNumber         saveReg_17           12                   null               \s
                saveAllRegToStack    null                 null                 null               \s
                saveToStack          saveReg_17           null                 null               \s
                assignString         saveReg_20           label_19             null               \s
                saveToStack          saveReg_20           null                 null               \s
                clearReg             fib                  null                 null               \s
                jump                 fib                  null                 null               \s
                label                label_19             null                 null               \s
                loadAllFromStack     null                 null                 null               \s
                assign               saveReg_18           pvmReg_returnValue   null               \s
                assign               pvmReg_returnValue   saveReg_18           null               \s
                jump                 returnLabel_16       null                 null               \s
                label                returnLabel_16       null                 null               \s
                undefineSymbol       saveReg_17           null                 null               \s
                undefineSymbol       saveReg_18           null                 null               \s
                undefineSymbol       saveReg_20           null                 null               \s
                """;
    }
}


