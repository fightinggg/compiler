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
                label                ifFunc     null       null     \s
                parmaLoad            x          null       null     \s
                assignNumber         symbol_0   1          null     \s
                equal                symbol_1   x          symbol_0 \s
                jFalse               label_4    symbol_1   null     \s
                assignNumber         symbol_2   1          null     \s
                return               symbol_2   null       null     \s
                jump                 label_5    symbol_1   null     \s
                label                label_4    null       null     \s
                assignNumber         symbol_3   2          null     \s
                return               symbol_3   null       null     \s
                label                label_5    null       null     \s
                label                fib        null       null     \s
                parmaLoad            x          null       null     \s
                assignNumber         symbol_6   2          null     \s
                lt                   symbol_7   x          symbol_6 \s
                jFalse               label_9    symbol_7   null     \s
                assignNumber         symbol_8   1          null     \s
                return               symbol_8   null       null     \s
                label                label_9    null       null     \s
                parmaPut             symbol_11  null       null     \s
                call                 symbol_12  fib        null     \s
                parmaPut             symbol_14  null       null     \s
                call                 symbol_15  fib        null     \s
                add                  symbol_16  symbol_12  symbol_15\s
                return               symbol_16  null       null     \s
                label                main       null       null     \s
                assignNumber         symbol_17  10         null     \s
                assign               a          symbol_17  null     \s
                assignNumber         symbol_18  2          null     \s
                assign               b          symbol_18  null     \s
                add                  symbol_19  a          b        \s
                assign               d          symbol_19  null     \s
                assignNumber         symbol_20  0          null     \s
                add                  symbol_21  d          symbol_20\s
                assign               e          symbol_21  null     \s
                parmaPut             symbol_22  null       null     \s
                call                 symbol_23  fib        null     \s
                parmaPut             symbol_24  null       null     \s
                call                 symbol_25  fib        null     \s
                add                  symbol_26  symbol_23  symbol_25\s
                assign               fib7       symbol_26  null     \s
                add                  symbol_27  a          b        \s
                jFalse               label_30   symbol_27  null     \s
                assignNumber         symbol_28  1          null     \s
                update               symbol_29  symbol_28  null     \s
                label                label_30   null       null     \s
                label                label_42   null       null     \s
                add                  symbol_31  a          b        \s
                jFalse               label_41   symbol_31  null     \s
                assignNumber         symbol_32  2          null     \s
                update               symbol_33  symbol_32  null     \s
                assignNumber         symbol_34  1          null     \s
                update               symbol_35  symbol_34  null     \s
                assignNumber         symbol_36  1          null     \s
                sub                  symbol_37  a          symbol_36\s
                update               symbol_38  symbol_37  null     \s
                assignNumber         symbol_39  0          null     \s
                update               symbol_40  symbol_39  null     \s
                label                label_42   null       null     \s
                label                label_41   null       null     \s
                assignNumber         symbol_43  0          null     \s
                assign               i          symbol_43  null     \s
                label                label_51   null       null     \s
                assignNumber         symbol_44  10         null     \s
                lt                   symbol_45  i          symbol_44\s
                jFalse               label_52   symbol_45  null     \s
                add                  symbol_49  s          i        \s
                update               symbol_50  symbol_49  null     \s
                assignNumber         symbol_46  1          null     \s
                add                  symbol_47  i          symbol_46\s
                update               symbol_48  symbol_47  null     \s
                jump                 label_51   null       null     \s
                label                label_52   null       null     \s
                assignNumber         symbol_53  0          null     \s
                assign               i          symbol_53  null     \s
                label                label_71   null       null     \s
                assignNumber         symbol_54  20         null     \s
                lt                   symbol_55  i          symbol_54\s
                jFalse               label_72   symbol_55  null     \s
                assignNumber         symbol_59  3          null     \s
                assign               x          symbol_59  null     \s
                label                label_67   null       null     \s
                jFalse               label_66   x          null     \s
                assignNumber         symbol_60  1          null     \s
                add                  symbol_61  c          symbol_60\s
                update               symbol_62  symbol_61  null     \s
                assignNumber         symbol_63  1          null     \s
                sub                  symbol_64  x          symbol_63\s
                update               symbol_65  symbol_64  null     \s
                label                label_67   null       null     \s
                label                label_66   null       null     \s
                assignNumber         symbol_68  1          null     \s
                add                  symbol_69  a          symbol_68\s
                update               symbol_70  symbol_69  null     \s
                assignNumber         symbol_56  1          null     \s
                add                  symbol_57  i          symbol_56\s
                update               symbol_58  symbol_57  null     \s
                jump                 label_71   null       null     \s
                label                label_72   null       null     \s
                return               fib7       null       null     \s
                """;
    }
}


