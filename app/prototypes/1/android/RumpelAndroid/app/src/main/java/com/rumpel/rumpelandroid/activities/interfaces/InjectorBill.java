package com.rumpel.rumpelandroid.activities.interfaces;


import java.math.BigDecimal;

public interface InjectorBill {
    void addItemBill(final String name, final BigDecimal price);
    void modifyItemBill(final String name, final BigDecimal price);

}
