package grammar.augment;

import grammar.Grammar;
import grammar.Production;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AugmentProductionItem {
    public static Set<AugmentProduction> closure(Grammar grammar, List<AugmentProduction> productions) {
        Set<AugmentProduction> collect = grammar.allProduction()
                .stream()
                .filter(production -> !production.rightSymbol().isEmpty())
                .filter(production ->
                        productions.stream()
                                .filter(o -> o.pos() != o.rightSymbol().size())
                                .anyMatch(o -> o.rightSymbol().get(o.pos()).equals(production.rightSymbol().get(0)))
                )
                .map(production -> new NormalAugmentProduction(production, 0))
                .collect(Collectors.toSet());

        collect.addAll(productions);

        return collect.stream().collect(Collectors.toUnmodifiableSet());
    }

    public static Set<AugmentProduction> itemGoto(Grammar grammar, Set<AugmentProduction> from, String symbol) {
        List<AugmentProduction> halfTarget = from.stream()
                .filter(o -> o.pos() != o.rightSymbol().size())
                .filter(production -> production.rightSymbol().get(production.pos()).equals(symbol))
                .map(o -> new NormalAugmentProduction(o, o.pos() + 1))
                .collect(Collectors.toList());
        return closure(grammar, halfTarget);
    }

}
