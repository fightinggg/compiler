package grammar;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author wsx
 */
@Data
public class JsonGrammar {
    /**
     * 产生式集合
     * k->[v1,v2,v3]
     * 所有的k必须为小写字母和大写字母组合，不允许使用其他的字符
     *
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
}
