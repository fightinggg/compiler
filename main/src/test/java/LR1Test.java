import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lexical.LexicalAnalysisImpl;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.syntaxtree.SyntaxTree;
import com.example.visiable.FileUtils;
import com.example.visiable.SyntaxTreeVisiable;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LR1Test {

    @Test
    public void test() {
        String code = "cccdcccccccd";
//
//        // 1. 获取词法和文法配置
//        LexicalConfig lexicalConfig = LexicalConfigReader.read("龙书-4.55.json", "");
//        GrammarConfig grammarConfig = GrammarReader.read("龙书-4.55.json", "");
//
//        // 2. 执行词法分析
//        List<Token> tokes = new LexicalAnalysisImpl().parsing(code, lexicalConfig);
//        FileUtils.writeFile("target/%s-tokens.txt".formatted(grammarConfig.name()),
//                tokes.stream().map(Objects::toString).collect(Collectors.joining("\n")));
//
//
//        // 3. 根据文法构建SLR语法分析器
//        LRTable lrTable = new SLRTableAnalyzer().analyze(grammarConfig);
//
//        // 4. 执行语法分析
//        final SyntaxTree syntaxTree = new LRAnalyzerImpl().analyze(lrTable, tokes);
//
//        final String syntaxTreeString = SyntaxTreeVisiable.toDot(syntaxTree);
//        FileUtils.writeFile("target/%s-syntaxTree.dot".formatted(grammarConfig.name()), syntaxTreeString);

    }



}
