package com.example.lexical;


import com.example.lang.reg.Reg;
import com.example.lang.reg.RegSyntaxDirectedTranslation;
import com.example.nfa.Nfa;
import com.example.nfa.NfaImpl;
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

        final Nfa<Object, String> parallel = NfaUtils.parallel(nfaList, RegSyntaxDirectedTranslation.EMPTY);

        final String nfa = NfaVisiable.nfa2Dot(parallel);

        DotUtils.writeDotFile("target/cppNfaRegs.dot", nfa);


        return null;
    }
}
