package com.example.lexical;


import com.example.lang.reg.Reg;
import com.example.nfa.*;
import com.example.visiable.FileUtils;
import com.example.visiable.NfaVisiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LexicalAnalysisImpl implements LexicalAnalysis {
    @Override
    public List<Token> parsing(String code, LexicalConfig lexicalConfig) {
        final List<Nfa<Object, String>> nfaList = lexicalConfig.getTokens().entrySet().stream()
                .map(kv -> {
//                    System.out.println(kv.getValue());
                    final Nfa<Object, String> nfa = Reg.parse(kv.getValue());
                    if (nfa instanceof NfaImpl) {
                        Map<Object, Function<List<String>, Object>> invoker = nfa.endStateSet().stream()
                                .collect(Collectors.toMap(end -> end, end -> strings -> new TokenImpl(kv.getKey(), String.join("", strings))));
                        ((NfaImpl<Object, String>) nfa).setEndStateInvoke(invoker);

                        Map<Object, Integer> orders = nfa.endStateSet().stream()
                                .collect(Collectors.toMap(end -> end, end -> lexicalConfig.tokenOrder(kv.getKey())));
                        ((NfaImpl<Object, String>) nfa).setEndStateInvokeOrder(orders);
                    }
                    return nfa;
                }).collect(Collectors.toList());

        Nfa<Object, String> nfa = NfaUtils.parallel(nfaList, Nfa.EMPTY_TRANS);

        nfa = Nfa2Dfa.toDfa(nfa);


        final String nfaDot = NfaVisiable.nfa2Dot(nfa);

        FileUtils.writeFile("target/cppNfaRegs.dot", nfaDot);


        final NfaMatcher<Object, String> matcher = new NfaMatcher<>(nfa, Nfa.EMPTY_TRANS);

        List<Token> list = new ArrayList<>();
        for (int i = 0; i < code.length(); i++) {
            final Map.Entry<Boolean, Object> eat = matcher.eat(String.valueOf(code.charAt(i)));
            if (eat.getKey()) {
                list.add((Token) eat.getValue());
                i--;
            } else {
            }
        }

        list.add((Token) matcher.end().getValue());

        return list;
    }
}
