package grammar;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * 产生式
 *
 * @author wsx
 */
@Data
public class Production {
    /**
     * 左边
     */
    private String from;

    /**
     * 右边
     */
    private List<String> derive;

}
