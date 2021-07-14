package grammar;

import lombok.Data;

import java.util.List;

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
