package com.rumpel.rumpeldesktop;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOTag;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

/**
 * Class for testing. Contains a method to insert random bills.
 */
public final class MassInserter {
    private MassInserter() {
        throw new AssertionError("Utility class");
    }
    
    public static void generateRandomBillsPerYear(int amount) {
        var pms = new DAOPaymentMethod().getAll();
        var random = new Random();
        var tags = new DAOTag().getAll();
        var bills = new DAOBill();
        var list = new ArrayList<Bill>(amount);
        for (int i = 0; i < amount; i++) {
            var randomItemNumber = random.nextInt(1, 11);
            var items = new ArrayList<ItemBill>(randomItemNumber);
            for (int j = 0; j < randomItemNumber; j++) {
                items.add(new ItemBill("item_" + (j + 1), BigDecimal.valueOf(random.nextInt(0, 1000)), List.of(
                        tags.get(random.nextInt(0,tags.size())))));
            }
            var e = Bill.of(Currency.getInstance("EUR"),
                    pms.get(random.nextInt(0, pms.size())), items);
            if (e.isEmpty()) throw new RuntimeException("uh oh - e is empty");
            e.get().setDate(
                    ZonedDateTime.of(
                        LocalDate.of(random.nextInt(2020, Year.now().getValue() + 1), random.nextInt(1, 13), random.nextInt(1, 29)),
                        LocalTime.NOON, ZoneId.systemDefault()
                    )
            );
            e.get().setUser(DAOUser.getLoggedUser());
            e.get().calcTotal();
            list.add(e.get());
        }
        System.out.println("About to insert");
        Outcome outcome = bills.insertAll(list);
        if (outcome == Outcome.ERROR) System.out.println("Errors!");
    }
}
