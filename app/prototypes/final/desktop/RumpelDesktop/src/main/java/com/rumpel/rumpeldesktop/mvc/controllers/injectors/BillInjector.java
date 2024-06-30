package com.rumpel.rumpeldesktop.mvc.controllers.injectors;

import com.szaumoor.rumple.model.entities.Bill;

/**
 * Injector to inject bill data from dialogs into the home view
 */
public interface BillInjector {
    void insertBill(final Bill bill);

    void modifyBill(Bill bill);
}
