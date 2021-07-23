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
import com.example.grammar.visiable.AugmentProductionItemSetVisiable;
import com.example.grammar.visiable.DotUtils;
import com.example.grammar.visiable.SyntaxTreeVisiable;
import com.example.lang.reg.RegLexicalAnalysisImpl;
import com.example.lexical.LexicalAnalysis;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
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
        GrammarConfig grammarConfig = GrammarReader.read("cpp.json");
        Map<String, Set<String>> followSet = GrammarFollowSet.followSet(grammarConfig);
        System.out.println(JSON.toJSONString(followSet, SerializerFeature.PrettyFormat));

        Map<Set<SLRAugmentProduction>, Map<String, Set<SLRAugmentProduction>>> map =
                SLRAugmentProductionItem.itemSetDfa(grammarConfig);

        String dotCode = AugmentProductionItemSetVisiable.toDot(map);

        DotUtils.writeDotFile("target/augmentStateDFA.dot", dotCode);

    }


    @Test
    public void RegTest() {

        String code = "a|bc+de|f*{1}(123)\\(\\)";
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

    }
}
