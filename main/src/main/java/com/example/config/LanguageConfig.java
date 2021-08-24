package com.example;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class LanguageConfig {
    /**
     * tokens的定义
     */
    List<Map<String, String>> tokens;


    /**
     * 空token(可删除)
     */
    private String blankToken;

    /**
     * 产生式集合
     * k->[v1,v2,v3]
     * 所有的k必须为小写字母和大写字母组合，不允许使用其他的字符
     */
    private Map<String, Set<String>> productionsTable;

    /**
     * 文法目标
     */
    private String target;


    private String name;

}
