import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.lr.LRAnalyzer;
import com.example.grammar.augment.lr.LRAnalyzerImpl;
import com.example.grammar.augment.lr.LRTable;
import com.example.grammar.augment.lr.LRTableAnalyzer;
import com.example.grammar.augment.lr.slr.SLRTableAnalyzer;
import com.example.lang.reg.Reg;
import com.example.lexical.LexicalAnalysis;
import com.example.lexical.LexicalConfig;
import com.example.lexical.LexicalConfigReader;
import com.example.lexical.Token;
import com.example.visiable.FileUtils;
import com.example.visiable.NfaVisiable;
import com.example.visiable.SyntaxTreeVisiable;
import com.example.lang.reg.RegLexicalAnalysisImpl;
import com.example.lang.reg.RegSyntaxDirectedTranslation;
import com.example.nfa.Nfa;
import com.example.syntaxtree.SyntaxTree;
import org.junit.Test;

import java.util.List;

public class CompilerTests {


    @Test
    public void regTest() {

        String code = "a|bc+de|f*(123)[456]?\\(\\)";
        System.out.println(Reg.parse(code, "CompilerTests.regTest"));

    }
}
