package grammar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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


        JSONObject jsonObject = JSON.parseObject(new String(code));

        Map<String, List<Production>> table = new HashMap<>();
        for (String productionRaw : jsonObject.keySet()) {
            JSONArray jsonArray = jsonObject.getJSONArray(productionRaw);

            List<Production> productions = jsonArray.toJavaList(String.class).stream()
                    .map(str -> " ".equals(str) ? Collections.singletonList(" ") : Arrays.asList(str.split(" ")))
                    .map(lists -> {
                        Production production = new Production();
                        production.setDerive(lists);
                        production.setFrom(productionRaw);
                        return production;
                    }).collect(Collectors.toList());

            table.put(productionRaw, productions);
        }

        Grammar grammar = new Grammar();
        grammar.setTable(table);
        grammar.setTarget("grammar");
        return grammar;
    }
}
