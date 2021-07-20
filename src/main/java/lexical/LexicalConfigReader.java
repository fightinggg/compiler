package lexical;

import com.alibaba.fastjson.JSON;
import grammar.JsonGrammarReader;

import java.io.IOException;
import java.io.InputStream;

public class LexicalConfigReader {
    public static LexicalConfig read(String path){
        byte[] code;
        try (InputStream inputStream = JsonGrammarReader.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            code = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return JSON.parseObject(new String(code), LexicalConfig.class);
    }
}
