package grammar.chomsky;

import grammar.GrammarConfig;
import grammar.Production;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChomskyGrammarConfig implements GrammarConfig {
    /**
     * 产生式集合
     */
    private Map<String, Set<ChomskyProduction>> productionsTable;

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
    public Set<Production> allProduction(String left) {
        return productionsTable.get(left).stream()
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
