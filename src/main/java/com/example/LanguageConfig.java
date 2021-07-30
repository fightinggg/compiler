package com.example;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class LanguageConfig {
    /**
     * tokens的定义
     */
    Map<String, String> tokens;

    /**
     * tokens的优先级
     */
    Map<String, Integer> tokenOrders;

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

    /**
     * 产生式重点关注对象
     */
    private Set<String> keys;

    private String name;

}
