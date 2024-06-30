import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.PaymentMethod;

import com.szaumoor.rumple.model.utils.Bools;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public final class TestBills {

    private static Currency defaultCurr;
    private static PaymentMethod defPm;
    private static Bill defBill;
    private static ItemBill defItem1;

    @BeforeAll
    public static void setUp() {
        defaultCurr = Currency.getInstance(Locale.getDefault());
        defPm = new PaymentMethod("def pm");
        defBill = new Bill();
        defItem1 = new ItemBill("Def Item 1", BigDecimal.TEN, defBill, null);
    }

    @Test
    public void newProperBillSucceeds() {
        final var bill = Bill.of(defaultCurr, defPm, List.of(defItem1));
        assertTrue(bill.isPresent(), "Bill failed at being created with proper args.");
    }

    @Test
    public void newBillFailsIfContainingNoItems() {
        final var bill = Bill.of(defaultCurr, defPm, List.of());
        assertTrue(bill.isEmpty(), "Bill succeeded at being created without items.");
    }

    @Test
    public void newBillFailsIfNullArgs() {
        final var bill = Bill.of(defaultCurr, null, List.of(defItem1));
        assertTrue(bill.isEmpty(), "Bill succeeded at being created with null args unexpectedly.");
    }

    @Test
    public void billFailsAtSettingFieldsToNull() {
        final boolean v1 = defBill.setCurrency(null);
        final boolean v2 = defBill.setDate(null);
        final boolean v3 = defBill.setPaymentMethod(null);
        assertTrue(Bools.allFalse(v1, v2, v3), "A setter succeeded at setting a null");
    }

    @Test
    public void calcWorksTwoItems() {
        final var b = new ItemBill("Banana", BigDecimal.valueOf(15.4), null, null);
        final var c = new ItemBill("Apple", BigDecimal.valueOf(10.1), null, null);
        final var bp = Bill.of(defaultCurr, defPm, List.of(b, c));
        final var bill = bp.orElseThrow();
        assertEquals(bill.calcTotal(), new BigDecimal("25.5"), "Calculation failed");
    }
}
