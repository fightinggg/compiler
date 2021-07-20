import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.grammar.GrammarConfig;
import com.example.grammar.GrammarFollowSet;
import com.example.grammar.GrammarReader;
import com.example.grammar.augment.AugmentProduction;
import com.example.grammar.augment.AugmentProductionItem;
import com.example.grammar.visiable.AugmentProductionItemSetVisiable;
import com.example.grammar.visiable.DotUtils;
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
        GrammarConfig grammarConfig = GrammarReader.read("reg.json");
        Map<String, Set<String>> followSet = GrammarFollowSet.followSet(grammarConfig);
        System.out.println(JSON.toJSONString(followSet, SerializerFeature.PrettyFormat));

        Map<Set<AugmentProduction>, Map<String, Set<AugmentProduction>>> map =
                AugmentProductionItem.itemSetDFA(grammarConfig);

        String dotCode = AugmentProductionItemSetVisiable.toDot(map);

        DotUtils.writeDotFile("target/augmentStateDFA.dot", dotCode);

    }
}
