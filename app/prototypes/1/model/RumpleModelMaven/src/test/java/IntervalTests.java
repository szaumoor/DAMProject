import com.szaumoor.rumple.model.utils.types.Interval;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntervalTests {

    private static ZonedDateTime now;

    @BeforeAll
    public static void setUp() {
        now = ZonedDateTime.now();
    }

    @Test
    public void canCreateNormalInterval() {
        var interval = Interval.of(now, now.plusDays(7));
        assertTrue(interval.isPresent(), "Interval couldn't be created");
    }

    @Test
    public void cannotCreateIntervalIfEndDateIsBeforeStartDate() {
        var interval = Interval.of(now, now.minusDays(2));
        assertTrue(interval.isEmpty(), "Interval could be created with improper dates");
    }

    @Test
    public void formatWorksAsExpected() {
        ZonedDateTime startDate = ZonedDateTime.of(LocalDateTime.of(2000, Month.APRIL, 1, 1, 0), ZoneId.systemDefault());
        var interval = Interval.of(startDate, startDate.plusDays(1));
        var mappings = interval.orElseThrow().format();
        String formattedStartDate = mappings.get(Interval.START_DATE);
        String formattedEndDate = mappings.get(Interval.END_DATE);
        boolean match = formattedStartDate.equals("01/04/2000") && formattedEndDate.equals("02/04/2000");
        assertTrue(match, "Formatted lines don't match: " + formattedStartDate + " - " + formattedEndDate + ". Expected 01/04/2000 - 02/04/2000");
    }
}
