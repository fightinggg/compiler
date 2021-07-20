import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import grammar.*;
import grammar.augment.AugmentProduction;
import grammar.augment.AugmentProductionItem;
import grammar.visiable.AugmentProductionItemSetVisiable;
import grammar.visiable.DotUtils;
import org.junit.Test;

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
        Map<String, Set<String>> followSet = GrammarFollowSet.followSet(grammar);
        System.out.println(JSON.toJSONString(followSet, SerializerFeature.PrettyFormat));

        Map<Set<AugmentProduction>, Map<String, Set<AugmentProduction>>> map =
                AugmentProductionItem.itemSetDFA(grammar);

        String dotCode = AugmentProductionItemSetVisiable.toDot(map);

        DotUtils.writeDotFile("target/augmentStateDFA.dot", dotCode);

        String s;
    }
}
