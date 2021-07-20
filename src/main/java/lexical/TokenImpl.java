package lexical;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TokenImpl implements Token {
    private String type;
    private String raw;

    @Override
    public String type() {
        return type;
    }

    @Override
    public String raw() {
        return raw;
    }
}
