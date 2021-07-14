package grammar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wsx
 */
public class ProductionFactory {
    public static Production fromString(String left, String right) {
        Production production = new Production();
        production.setFrom(left);
        production.setDerive(" ".equals(right) ? Collections.singletonList(" ") : Arrays.asList(right.split(" ")));
        return production;
    }

    public static List<Production> fromStringList(String left, List<String> right) {
        return right.stream()
                .map(o -> fromString(left, o))
                .collect(Collectors.toList());
    }

}
