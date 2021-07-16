package grammar;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文法
 *
 * @author wsx
 */
@Data
public class Grammar {
    /**
     * 产生式集合
     */
    private Map<String, Set<Production>> productionsTable;

    /**
     * 文法目标
     */
    private String target;

    /**
     * 产生式重点关注对象
     */
    private Set<String> keys;
}
