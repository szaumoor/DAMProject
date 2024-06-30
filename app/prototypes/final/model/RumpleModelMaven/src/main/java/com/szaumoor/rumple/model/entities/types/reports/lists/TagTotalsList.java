package com.szaumoor.rumple.model.entities.types.reports.lists;

import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.Tag;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of a total list for tags
 */
public final class TagTotalsList extends TotalsList<Tag> {
    private final List<ItemBill> itemsToCheck;

    /**
     * Creates a list of totals for the provided tags and tags.
     *
     * @param tags The tags
     * @param itemsToCheck The tags
     */
    public TagTotalsList(final List<Tag> tags, final List<ItemBill> itemsToCheck) {
        super(tags);
        this.itemsToCheck = itemsToCheck;
    }

    /**
     * Calculates the totals of expenses per tag
     */
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
