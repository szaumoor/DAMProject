package com.szaumoor.rumple.model.entities.types.reports;


import com.szaumoor.rumple.model.entities.Bill;

import java.util.List;
import java.util.Vector;

/**
 * Skeleton for a basic report class.
 */
public abstract class AbstractReport {
    protected boolean infoSet; // Tracks if the report content has been set previously
    protected List<Bill> bills;

    /**
     * No-arg constructor, makes the bill list a vector of 100 elements.
     * It's a vector in this case because it's a synchronized list, so issues with concurrent modification
     * are avoided.
     */
    public AbstractReport() {
        bills = new Vector<>(100);
    }

    /**
     * Constructor that sets the infoSet flag if needed (this is probably nonsensical, on second thought)
     *
     * @param infoSet the infoSet flag
     */
    protected AbstractReport(final boolean infoSet) {
        this();
        this.infoSet = infoSet;
    }

    /**
     * Adds all bills to the report. Automatically sets the infoSet flag to true.
     *
     * @param bills the bills to add
     * @return true or false as per the result of the addAll() method call on the Vector object
     */
    public boolean addAll(final List<Bill> bills) {
        infoSet = true;
        return this.bills.addAll(bills);
    }
}
