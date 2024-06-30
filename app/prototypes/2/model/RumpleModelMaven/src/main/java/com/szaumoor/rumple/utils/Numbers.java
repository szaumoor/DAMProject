package com.szaumoor.rumple.utils;

import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum Numbers {
    ;

    public static boolean limitsValid(final BigDecimal softLimit, final BigDecimal hardLimit) {
        return Nulls.nonNull(softLimit, hardLimit) &&
                softLimit.compareTo(BigDecimal.ZERO) >= 0 &&
                hardLimit.compareTo(softLimit) > 0;
    }

    public static boolean limitsValid(final int softLimit, final int hardLimit) {
        return softLimit > 0 && hardLimit > softLimit;
    }

    public static boolean limitsValid(final long softLimit, final long hardLimit) {
        return softLimit > 0 && hardLimit > softLimit;
    }

    public static boolean limitsValid(final double softLimit, final double hardLimit) {
        return softLimit > 0 && hardLimit > softLimit;
    }

    public static boolean limitsValid(final float softLimit, final float hardLimit) {
        return softLimit > 0 && hardLimit > softLimit;
    }

    public static BigDecimal sum(final BigDecimal ... nums) {
        return Arrays.stream(nums).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sum(final List<BigDecimal> nums) {
        return nums.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sum(final Bill ... bills) {
        return Arrays.stream(bills)
                .map(Bill::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sum(final ItemBill... items) {
        return Arrays.stream(items)
                .map(ItemBill::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sum(final Collection<ItemBill> items) {
        return items.stream()
                .map(ItemBill::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
