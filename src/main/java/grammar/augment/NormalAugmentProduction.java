package grammar.augment;

import com.alibaba.fastjson.JSON;
import grammar.NormalProduction;
import grammar.Production;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class NormalAugmentProduction extends NormalProduction implements AugmentProduction {
    private final int pos;

    public NormalAugmentProduction(Production production) {
        this(production, 0);
    }

    public NormalAugmentProduction(Production production, int pos) {
        this(production.leftSymbol(), production.rightSymbol(), pos);
    }

    public NormalAugmentProduction(String left, List<String> right) {
        this(left, right, 0);
    }


    public NormalAugmentProduction(String left, List<String> right, int pos) {
        super(left, right);
        this.pos = pos;
    }


    @Override
    public int pos() {
        return pos;
    }


    @Override
    public String toString() {
        String s = JSON.toJSONString(String.join(" ", rightSymbol()));
        return "%s -> %s , %d".formatted(leftSymbol(), s.substring(1, s.length() - 1), pos);
    }
}
