import com.szaumoor.rumple.model.entities.types.Limit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LimitTests {

    @Test
    public void limitSucceedsNormally() {
        var limit = Limit.of(new BigDecimal("1000.0"), new BigDecimal("2000.0"), Currency.getInstance(Locale.getDefault()));
        assertTrue(limit.isPresent(), "Limit failed to be built successfully");
    }

    @Test
    public void limitFailsIfNegSoftLimit() {
        var limit = Limit.of(new BigDecimal("-1000.0"), new BigDecimal("2000.0"), Currency.getInstance(Locale.getDefault()));
        assertTrue(limit.isEmpty(), "Limit succeeded at being created unexpectedly with a negative soft limit");
    }

    @Test
    public void limitFailsIfNegHardLimit() {
        var limit = Limit.of(new BigDecimal("1000.0"), new BigDecimal("-2000.0"), Currency.getInstance(Locale.getDefault()));
        assertTrue(limit.isEmpty(), "Limit succeeded at being created unexpectedly with a negative hard limit");
    }


    @Test
    public void limitFailsIfHardLimitBelowSoftLimit() {
        var limit = Limit.of(new BigDecimal("1000.0"), new BigDecimal("500.0"), Currency.getInstance(Locale.getDefault()));
        assertTrue(limit.isEmpty(), "Limit succeeded at being created unexpectedly with a soft limit greater than a hard limit");
    }

    @Test
    public void limitFailsWithNullDecimalLimits() {
        var limit = Limit.of(null, null, Currency.getInstance(Locale.getDefault()));
        assertTrue(limit.isEmpty(), "Limit succeeded at being created unexpectedly with null decimal limits");
    }

    @Test
    public void limitFailsWithNullCurrency() {
        var limit = Limit.of(new BigDecimal("1000.0"), new BigDecimal("500.0"), null);
        assertTrue(limit.isEmpty(), "Limit succeeded at being created unexpectedly with null currency");
    }

    @Test
    public void limitSecondWarningGetsExpectedResult() {
        var limit = Limit.of(new BigDecimal("1000.0"), new BigDecimal("2000.0"), Currency.getInstance(Locale.getDefault()));
        BigDecimal secondWarning = limit.orElseThrow().getSecondWarning();
        boolean result = secondWarning.equals(new BigDecimal("1500.0"));
        assertTrue(result, "Second warning was incorrect: expected 1500.0, but got " + secondWarning);
    }

}
