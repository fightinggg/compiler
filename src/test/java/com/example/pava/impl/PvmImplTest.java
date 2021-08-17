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
                label                ifFunc     null       null     
                parmaLoad            x          null       null     
                assignNumber         symbol_0   1          null     
                equal                symbol_1   a          symbol_0 
                jFalse               label_4    symbol_1   null     
                assignNumber         symbol_2   1          null     
                return               symbol_2   null       null     
                jump                 label_5    symbol_1   null     
                label                label_4    null       null     
                assignNumber         symbol_3   2          null     
                return               symbol_3   null       null     
                label                label_5    null       null     
                label                fib        null       null     
                parmaLoad            x          null       null     
                assignNumber         symbol_6   2          null     
                lt                   symbol_7   x          symbol_6 
                jFalse               label_9    symbol_7   null     
                assignNumber         symbol_8   1          null     
                return               symbol_8   null       null     
                label                label_9    null       null     
                parmaPut             symbol_11  null       null     
                call                 symbol_12  fib        null     
                parmaPut             symbol_14  null       null     
                call                 symbol_15  fib        null     
                add                  symbol_16  symbol_12  symbol_15
                return               symbol_16  null       null     
                label                main       null       null     
                assignNumber         symbol_17  1          null     
                assign               a          symbol_17  null     
                assignNumber         symbol_18  2          null     
                assign               b          symbol_18  null     
                add                  symbol_19  a          b        
                assign               d          symbol_19  null     
                assignNumber         symbol_20  0          null     
                add                  symbol_21  d          symbol_20
                assign               e          symbol_21  null     
                assignString         symbol_22  "abc"      null     
                assign               s          symbol_22  null     
                call                 symbol_23  f1         null     
                parmaPut             symbol_24  null       null     
                call                 symbol_25  f2         null     
                add                  symbol_26  symbol_23  symbol_25
                assign               invoke     symbol_26  null     
                add                  symbol_27  a          b        
                jFalse               label_30   symbol_27  null     
                assignNumber         symbol_28  1          null     
                update               symbol_29  symbol_28  null     
                label                label_30   null       null     
                label                label_37   null       null     
                add                  symbol_31  a          b        
                jFalse               label_36   symbol_31  null     
                assignNumber         symbol_32  2          null     
                update               symbol_33  symbol_32  null     
                assignNumber         symbol_34  1          null     
                update               symbol_35  symbol_34  null     
                label                label_37   null       null     
                label                label_36   null       null     
                assignNumber         symbol_38  0          null     
                assign               i          symbol_38  null     
                label                label_44   null       null     
                jFalse               label_45   i          null     
                add                  symbol_42  s          i        
                update               symbol_43  symbol_42  null     
                assignNumber         symbol_39  1          null     
                add                  symbol_40  i          symbol_39
                update               symbol_41  symbol_40  null     
                jump                 label_44   null       null     
                label                label_45   null       null     
                assignNumber         symbol_46  0          null     
                assign               i          symbol_46  null     
                label                label_58   null       null     
                jFalse               label_59   i          null     
                label                label_54   null       null     
                jFalse               label_53   i          null     
                assignNumber         symbol_50  1          null     
                add                  symbol_51  c          symbol_50
                update               symbol_52  symbol_51  null     
                label                label_54   null       null     
                label                label_53   null       null     
                assignNumber         symbol_55  1          null     
                add                  symbol_56  a          symbol_55
                update               symbol_57  symbol_56  null     
                assignNumber         symbol_47  1          null     
                add                  symbol_48  i          symbol_47
                update               symbol_49  symbol_48  null     
                jump                 label_58   null       null     
                label                label_59   null       null     
                """;
    }
}


