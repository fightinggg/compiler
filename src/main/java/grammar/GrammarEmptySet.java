package grammar;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GrammarEmptySet {
    public static Set<String> emptySet(Grammar grammar) {
        Set<String> emptySet = new HashSet<>();
        while (true) {
            Set<String> finalEmptySet = emptySet;
            Set<String> newEmptySet = grammar.allProduction().stream()
                    .filter(o -> finalEmptySet.containsAll(o.rightSymbol()))
                    .map(Production::leftSymbol)
                    .collect(Collectors.toSet());
            if (emptySet.size() == newEmptySet.size()) {
                break;
            } else {
                emptySet = newEmptySet;
            }
        }
        return emptySet;
    }
}
