package com.example.lexical;


import com.example.lang.reg.Reg;
import com.example.nfa.Nfa;
import com.example.nfa.NfaImpl;
import com.example.nfa.NfaMatcher;
import com.example.nfa.NfaUtils;
import com.example.visiable.DotUtils;
import com.example.visiable.NfaVisiable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LexicalAnalysisImpl implements LexicalAnalysis {
    @Override
    public List<Token> parsing(String code, LexicalConfig lexicalConfig) {
        final List<Nfa<Object, String>> nfaList = lexicalConfig.getTokens().entrySet().stream()
                .map(kv -> {
                    System.out.println(kv.getValue());
                    final Nfa<Object, String> nfa = Reg.parse(kv.getValue());
                    if (nfa instanceof NfaImpl) {
                        Map<Object, Function<List<String>, Object>> invoker = nfa.endStateSet().stream()
                                .collect(Collectors.toMap(end -> end, end -> strings -> new TokenImpl(kv.getKey(), String.join("", strings))));
                        ((NfaImpl<Object, String>) nfa).setEndStateInvoke(invoker);
                    }
                    return nfa;
                }).collect(Collectors.toList());

        final Nfa<Object, String> nfa = NfaUtils.parallel(nfaList, Nfa.EMPTY_TRANS);

        final String nfaDot = NfaVisiable.nfa2Dot(nfa);

        DotUtils.writeDotFile("target/cppNfaRegs.dot", nfaDot);


        final NfaMatcher<Object, String> matcher = new NfaMatcher<>(nfa, Nfa.EMPTY_TRANS);

        for (int i = 0; i < code.length(); i++) {
            final Map.Entry<Boolean, Object> eat = matcher.eat(String.valueOf(code.charAt(i)));
            if (eat.getKey()) {
                System.out.println(eat.getValue());
                i--;
            } else {
            }
        }

        return null;
    }
}
