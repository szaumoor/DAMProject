import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.utils.Bools;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestItemBills {

    private static String item;
    private static BigDecimal neg;
    private static BigDecimal pos;
    private static Tag defTag;

    @BeforeAll
    public static void setUp() {
        item = "Item";
        pos = new BigDecimal("10.0");
        neg = new BigDecimal("-5.4");
        defTag = new Tag("Food");
    }


    @Test
    public void canCreateItemBillNormally() {
        final var it = ItemBill.of(item, pos, List.of(defTag));
        assertTrue(it.isPresent(), "Couldn't create proper item");
    }

    @Test
    public void cannotCreateItemBillWithNegativePrice() {
        final var it = ItemBill.of(item, neg, List.of(defTag));
        assertTrue(it.isEmpty(), "An item bill with negative price was created");
    }

    @Test
    public void cannotSetNullPrices() {
        final var it = ItemBill.of(item, pos, List.of(defTag));
        final AtomicBoolean b = new AtomicBoolean(true);
        it.ifPresent(itemBill -> b.set(itemBill.setPrice(null)));
        assertFalse(b.get(), "Setting price to null was possible");
    }

    @Test
    public void cannotSetNegativePrices() {
        final var it = ItemBill.of(item, pos, List.of(defTag));
        final AtomicBoolean b = new AtomicBoolean(true);
        it.ifPresent(itemBill -> b.set(itemBill.setPrice(new BigDecimal("-5.4"))));
        assertFalse(b.get(), "Setting price to negative numbers was possible");
    }

    @Test
    public void cannotSetNullName() {
        final var it = ItemBill.of(item, pos, List.of(defTag));
        final AtomicBoolean b = new AtomicBoolean(true);
        it.ifPresent(itemBill -> b.set(itemBill.setName(null)));
        assertFalse(b.get(), "Setting name to null was possible");
    }

    @Test
    public void cannotSetEmptyOrOnlyWhitespaceName() {
        final var it = ItemBill.of(item, pos, List.of(defTag));
        final AtomicBoolean b = new AtomicBoolean(true);
        final AtomicBoolean c = new AtomicBoolean(true);
        it.ifPresent(itemBill -> {
            b.set(itemBill.setName(""));
            c.set(itemBill.setName("         "));
        });
        assertTrue(Bools.anyFalse(b.get(), c.get()), "Setting name to null or blank was possible");
    }

    @Test
    public void cannotCreateItemWithNulls() {
        final var banana = ItemBill.of(null, null, null);
        assertTrue(banana.isEmpty(), "Could create an improper item bill");
    }

}
