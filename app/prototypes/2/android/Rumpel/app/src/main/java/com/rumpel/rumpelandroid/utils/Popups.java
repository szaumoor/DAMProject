package com.rumpel.rumpelandroid.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * Singleton class with static utility methods for showing popups in Android, such as Toasts and Snackbars.
 */

public enum Popups {
    ;

    /**
     * Shows an Android Toast in the current context for the predetermined short duration.
     *
     * @param context Context in which the Toast will pop up.
     * @param msg The String containing the message to show.
     */
    public static void toast(final Context context, final String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows an Android Toast in the current context for the predetermined short duration.
     *
     * @param context Context in which the Toast will pop up.
     * @param msg The integer that points to the message to show.
     */
    public static void toast(final Context context, final int msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows an Android Toast in the current context for the predetermined long duration.
     *
     * @param context Context in which the Toast will pop up.
     * @param msg The String containing the message to show.
     */
    public static void toastLg(final Context context, final String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows an Android Toast in the current context for the predetermined long duration.
     *
     * @param context Context in which the Toast will pop up.
     * @param msg The integer that points to the message to show.
     */
    public static void toastLg(final Context context, final int msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows an Android Snackbar with default configuration in a particular view for the predetermined short duration.
     *
     * @param context The view in which the Snackbar will pop up.
     * @param msg The String containing the message to show.
     */
    public static void snackbar(final View context, final String msg) {
        Snackbar.make(context, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Shows an Android Snackbar with default configuration in a particular view for the predetermined short duration.
     *
     * @param context The view in which the Snackbar will pop up.
     * @param msg The integer that points to the message to show.
     */
    public static void snackbar(final View context, final int msg) {
        Snackbar.make(context, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Shows an Android Snackbar with default configuration in a particular view for the predetermined long duration.
     *
     * @param context The view in which the Snackbar will pop up.
     * @param msg The String containing the message to show.
     */
    public static void snackbarLg(final View context, final String msg) {
        Snackbar.make(context, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Shows an Android Snackbar with default configuration in a particular view for the predetermined long duration.
     *
     * @param context The view in which the Snackbar will pop up.
     * @param msg The integer that points to the message to show.
     */
    public static void snackbarLg(final View context, final int msg) {
        Snackbar.make(context, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Shows an Android Snackbar with default configuration in a particular view indefinitely.
     *
     * @param context The view in which the Snackbar will pop up.
     * @param msg The String containing the message to show.
     */
    public static void snackbarPerm(final View context, final String msg) {
        Snackbar.make(context, msg, Snackbar.LENGTH_INDEFINITE).show();
    }

    /**
     * Shows an Android Snackbar with default configuration in a particular view indefinitely.
     *
     * @param context The view in which the Snackbar will pop up.
     * @param msg The integer that points to the message to show.
     */
    public static void snackbarPerm(final View context, final int msg) {
        Snackbar.make(context, msg, Snackbar.LENGTH_INDEFINITE).show();
    }

}
