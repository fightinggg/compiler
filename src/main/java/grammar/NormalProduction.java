package grammar;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
public class NormalProduction implements Production {
    private final String left;
    private final List<String> right;

    public NormalProduction(String left, List<String> right){
        this.left = left;
        // 不可变对象
        this.right = right.stream().toList();
    }

    @Override
    public String leftSymbol() {
        return left;
    }

    @Override
    public List<String> rightSymbol() {
        return right;
    }
}
