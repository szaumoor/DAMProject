package com.rumpel.rumpeldesktop.mvc.controllers.interfaces;

import com.szaumoor.rumple.model.entities.ItemBill;

import java.util.List;

public interface ItemInjector {
    void insertItems(final List<ItemBill> items);
}
