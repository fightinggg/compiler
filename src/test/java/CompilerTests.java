import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFollowSet;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzer;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.grammar.augment.lr.slr.SLRAugmentProduction;
import com.example.grammar.augment.lr.slr.SLRAugmentProductionItem;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lexical.*;
import com.example.visiable.AugmentProductionItemSetVisiable;
import com.example.visiable.DotUtils;
import com.example.visiable.NfaVisiable;
import com.example.visiable.SyntaxTreeVisiable;
import com.example.lang.reg.RegLexicalAnalysisImpl;
import com.example.lang.reg.RegSyntaxDirectedTranslation;
import com.example.nfa.Nfa;
import com.example.syntaxtree.SyntaxTree;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompilerTests {

    String cppCode = "    int a = 1;\n" +
            "    int a = 1;\n" +
            "    int a = 1;\n" +
            "\n";

    @Test
    public void cppTest() {
        // 1. 获取词法和文法配置
        LexicalConfig lexicalConfig = LexicalConfigReader.read("cpp.json");
        GrammarConfig grammarConfig = GrammarReader.read("cpp.json");

        // 2. 根据文法构建SLR语法分析器
        LRTable lrTable = new SLRTableAnalyzer().analyze(grammarConfig);

        // 3. 执行词法分析
        List<Token> tokes = new LexicalAnalysisImpl().parsing(cppCode, lexicalConfig);

    }


    @Test
    public void RegTest() {

        String code = "a|bc+de|f*(123)[456]?\\(\\)";
        LexicalConfig lexicalConfig = LexicalConfigReader.read("reg.json");
        LexicalAnalysis lexicalAnalysis = new RegLexicalAnalysisImpl();
        List<Token> tokes = lexicalAnalysis.parsing(code, lexicalConfig);
//        System.out.println(JSON.toJSONString(tokes.stream().map(Object::toString).toList(),
//                SerializerFeature.PrettyFormat));

        GrammarConfig grammarConfig = GrammarReader.read("reg.json");
        Map<String, Set<String>> followSet = GrammarFollowSet.followSet(grammarConfig);
//        System.out.println(JSON.toJSONString(followSet, SerializerFeature.PrettyFormat));

        Map<Set<SLRAugmentProduction>, Map<String, Set<SLRAugmentProduction>>> map =
                SLRAugmentProductionItem.itemSetDfa(grammarConfig);

        String dotCode = AugmentProductionItemSetVisiable.toDot(map);

        DotUtils.writeDotFile("target/augmentStateDFA.dot", dotCode);


        LRTableAnalyzer lrTableAnalyzer = new SLRTableAnalyzer();
        LRTable lrTable = lrTableAnalyzer.analyze(grammarConfig);
        System.out.println(lrTable);


        LRAnalyzer lrAnalyzer = new LRAnalyzerImpl();
        SyntaxTree syntaxTree = lrAnalyzer.analyze(lrTable, tokes);

        String s = SyntaxTreeVisiable.toDot(syntaxTree);

        DotUtils.writeDotFile("target/syntaxtree.dot", s);


        Nfa<Object, String> nfa = RegSyntaxDirectedTranslation.toNfa(syntaxTree);

        String nfaString = NfaVisiable.nfa2Dot(nfa);

        DotUtils.writeDotFile("target/nfa.dot", nfaString);


        System.out.println(nfa);

    }
}
