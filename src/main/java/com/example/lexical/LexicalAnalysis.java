package com.example.lexical;

import java.util.List;

/**
 * 词法分析器
 *
 * @author s
 */
public interface LexicalAnalysis {
    /**
     * 从源代码解析词法流
     *
     * @param code
     * @param lexicalConfig
     * @return
     */
    List<Token> parsing(String code, LexicalConfig lexicalConfig);
}