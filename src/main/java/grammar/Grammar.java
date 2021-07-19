package grammar;

import java.util.Set;

/**
 * 文法四要素
 * 1. 终结符集合
 * 2. 非终结符集合
 * 3. 产生式集合
 * 4。目标
 */
public interface Grammar {

    Set<String> allTerminal();

    Set<String> allNotTerminal();

    Set<Production> allProduction();

    Set<Production> allProduction(String left);

    String target();

    boolean isTerminal(String symbol);
}
