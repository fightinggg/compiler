package grammar;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wsx
 */
public class JsonGrammarReader {
    public static Grammar read(String path) {
        byte[] code;
        try (InputStream inputStream = JsonGrammarReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        JsonGrammar jsonGrammar = JSON.parseObject(new String(code), JsonGrammar.class);

        Grammar grammar = new Grammar();

        grammar.setTarget(jsonGrammar.getTarget());
        grammar.setKeys(jsonGrammar.getKeys());
        Map<String, List<Production>> productions = jsonGrammar.getProductionsTable()
                .entrySet()
                .stream()
                .map(o -> Map.entry(o.getKey(), ProductionFactory.fromStringList(o.getKey(), o.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        grammar.setProductionsTable(productions);

        return grammar;
    }
}
