package grammar.augment;

import grammar.NormalProduction;
import grammar.Production;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = false)
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
}
