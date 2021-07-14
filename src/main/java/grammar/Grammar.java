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
    private Map<String, List<Production>> table;

    // 终结符集合
    // private Set<String> end;

    /**
     * 文法目标
     */
    private String target;
}
