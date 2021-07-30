import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzer;
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

public class MysqlTest {

    String code = "select * from users where id = 1  ";

    @Test
    public void mysqlTest() {
        // 1. 获取词法和文法配置
        LexicalConfig lexicalConfig = LexicalConfigReader.read("mysql.json");
        GrammarConfig grammarConfig = GrammarReader.read("mysql.json");

        // 2. 根据文法构建SLR语法分析器
        LRTable lrTable = new SLRTableAnalyzer().analyze(grammarConfig);

        // 3. 执行词法分析
        List<Token> tokes = new LexicalAnalysisImpl().parsing(code, lexicalConfig);

        // System.out.println(tokes.stream().map(Object::toString).collect(Collectors.joining("\n")));

        // 4. 执行语法分析
        final SyntaxTree syntaxTree = new LRAnalyzerImpl().analyze(lrTable, tokes);


        final String syntaxTreeString = SyntaxTreeVisiable.toDot(syntaxTree);
        FileUtils.writeFile("target/%s-syntaxTree.dot".formatted(grammarConfig.name()), syntaxTreeString);

    }

}
