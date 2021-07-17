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

    static List<String> string2List(String s){
        if(s.isEmpty()){
            return new ArrayList<>();
        }else if(s.length()==1){
            return new ArrayList<>(List.of(s));
        }else{
            return new ArrayList<>(Arrays.asList(s.split(" ")));
        }
    }

    public static NormalGrammar read(String path) {
        byte[] code;
        try (InputStream inputStream = JsonGrammarReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        JsonGrammar jsonGrammar = JSON.parseObject(new String(code), JsonGrammar.class);

        NormalGrammar normalGrammar = new NormalGrammar();

        normalGrammar.setTarget(jsonGrammar.getTarget());
        normalGrammar.setKeys(jsonGrammar.getKeys());


        Map<String, Set< List<String>>> productions = jsonGrammar.getProductionsTable()
                .entrySet()
                .stream()
                .map(o -> Map.entry(o.getKey(), o.getValue().stream().map(JsonGrammarReader::string2List).collect(Collectors.toSet())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        normalGrammar.setProductionsTable(productions);

        return normalGrammar;
    }
}
