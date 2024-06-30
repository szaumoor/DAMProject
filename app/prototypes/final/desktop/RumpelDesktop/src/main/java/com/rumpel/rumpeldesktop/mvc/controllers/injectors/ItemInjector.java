package com.rumpel.rumpeldesktop.mvc.controllers.injectors;

import com.szaumoor.rumple.model.entities.ItemBill;

import java.util.List;

/**
 * Injector to inject item data between dialogs
 */
public interface ItemInjector {
    void insertItems(final List<ItemBill> items);
}
