import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import grammar.Grammar;
import grammar.GrammarFirstSet;
import grammar.NormalGrammar;
import grammar.JsonGrammarReader;
import org.junit.Test;
import syntaxtree.SyntaxTree;
import syntaxtree.visiable.SyntaxTreeVisiable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class CompilerTests {

    String cppCode = "    int a = 1;\n" +
            "    int a = 1;\n" +
            "    int a = 1;\n" +
            "\n";

    @Test
    public void DfsParsingTest() {
        Grammar grammar = JsonGrammarReader.read("cpp-lex.json");
        Map<String, Set<String>> firstSet = GrammarFirstSet.firstSet(grammar);
        System.out.println(JSON.toJSONString(firstSet, SerializerFeature.PrettyFormat));

        String s;
    }
}
