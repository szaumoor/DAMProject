package com.rumpel.rumpeldesktop.mvc.controllers.interfaces;

import com.szaumoor.rumple.model.entities.Bill;

public interface BillInjector {
    void insertBill(final Bill bill);
    void modifyBill(Bill bill);
}
