package grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wsx
 */
public class ProductionFactory {
    public static Production fromString(String left, String right) {
        Production production = new Production();
        production.setFrom(left);
        production.setDerive(" ".equals(right) ? List.of(" ") : Arrays.asList(right.split(" ")));
        production.setDerive(new ArrayList<>(production.getDerive()));
        production.getDerive().removeIf(o -> o.length() == 0);
        return production;
    }

    public static Set<Production> fromStringList(String left, Set<String> right) {
        return right.stream()
                .map(o -> fromString(left, o))
                .collect(Collectors.toSet());
    }

}
