package grammar;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wsx
 */
public class JsonGrammarReader {

    static List<String> string2List(String s) {
        if (s.isEmpty()) {
            return new ArrayList<>();
        } else if (s.length() == 1) {
            return new ArrayList<>(List.of(s));
        } else {
            return new ArrayList<>(Arrays.asList(s.split(" ")));
        }
    }

    public static NormalGrammarConfig read(String path) {
        byte[] code;
        try (InputStream inputStream = JsonGrammarReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        JsonGrammar jsonGrammar = JSON.parseObject(new String(code), JsonGrammar.class);


        Map<String, Set<Production>> productions = jsonGrammar.getProductionsTable()
                .entrySet()
                .stream()
                .flatMap(o -> o.getValue().stream().map(v -> new NormalProduction(o.getKey(), string2List(v))))
                .collect(Collectors.groupingBy(NormalProduction::leftSymbol, Collectors.toSet()));


        return new NormalGrammarConfig(productions, jsonGrammar.getTarget());
    }
}
