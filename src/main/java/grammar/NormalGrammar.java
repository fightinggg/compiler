package grammar;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文法
 *
 * @author wsx
 */
public class NormalGrammar implements Grammar {
    /**
     * 产生式集合
     */
    private Map<String, Set<Production>> productionsTable;

    /**
     * 文法目标
     */
    private String target;

    @Override
    public Set<String> allTerminal() {
        throw new RuntimeException();
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
