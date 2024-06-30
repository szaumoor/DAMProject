import com.szaumoor.rumple.utils.Money;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherTests {

    @Test
    public void test() throws IOException, ParseException {
        BigDecimal quantity = new BigDecimal("10.45");
        String formattedCurr = Money.formatCurrency(quantity, Currency.getInstance("USD"));
        System.out.println(formattedCurr);
        Optional<BigDecimal> n = Money.unformatCurrency(formattedCurr);
        assertEquals(n.get().doubleValue(), quantity.doubleValue(), "not equal: " + quantity + " vs " + n);
    }
}
