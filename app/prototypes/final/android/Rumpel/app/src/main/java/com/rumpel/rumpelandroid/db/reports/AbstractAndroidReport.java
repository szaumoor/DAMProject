package com.rumpel.rumpelandroid.db.reports;

import android.content.Context;

import com.szaumoor.rumple.model.entities.types.reports.AbstractReport;

/**
 * Android specific base abstract class for reports. Only exists because a Context is required
 * in Android.
 */
public abstract class AbstractAndroidReport extends AbstractReport {
    protected final Context context;

    /**
     * Constructor for all Android reports that passes the context of the app.
     *
     * @param context the context of the app
     */
    protected AbstractAndroidReport(final Context context) {
        this(context, false);
    }

    /**
     * Constructor for all Android reports that passes the context of the app.
     *
     * @param context the context of the app
     * @param infoSet whether or not the info is set
     */
    protected AbstractAndroidReport(final Context context, final boolean infoSet) {
        super(infoSet);
        this.context = context;
    }
}
