package grammar;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文法
 *
 * @author wsx
 */
public class NormalGrammar implements Grammar {
    /**
     * 产生式集合
     */
    private final Map<String, Set<Production>> productionsTable;

    /**
     * 文法目标
     */
    private final String target;

    public NormalGrammar(Map<String, Set<Production>> productionsTable, String target) {
        this.productionsTable = productionsTable;
        this.target = target;
    }

    @Override
    public Set<String> allTerminal() {
        return productionsTable.values()
                .stream()
                .flatMap(Collection::stream)
                .flatMap(o -> o.rightSymbol().stream())
                .filter(o -> o.length() <= 1)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> allNotTerminal() {
        throw new RuntimeException();
    }

    @Override
    public Set<Production> allProduction() {
        return productionsTable.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String target() {
        return target;
    }

    @Override
    public boolean isTerminal(String symbol) {
        throw new RuntimeException();
    }
}
