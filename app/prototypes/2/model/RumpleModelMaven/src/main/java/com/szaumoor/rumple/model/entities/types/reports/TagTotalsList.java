package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.Tag;

import java.math.BigDecimal;
import java.util.List;

public final class TagTotalsList extends TotalsList<Tag> {
    private final List<ItemBill> itemsToCheck;

    public TagTotalsList(final List<Tag> items, final List<ItemBill> itemsToCheck) {
        super(items);
        this.itemsToCheck = itemsToCheck;
    }

    @Override
    public void calculate() {
        items.forEach(tag -> {
            var totalForTag = itemsToCheck.stream()
                    .filter(item -> item.contains(tag))
                    .map(ItemBill::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.add(new Total(tag.getName(), totalForTag));
        });
    }
}
