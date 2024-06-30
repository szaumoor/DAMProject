package com.rumpel.rumpelandroid.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

public enum AndroidUtils {
    ;
    public static void toast(final Context context, final String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(final Context context, final int msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(final Context context, final String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void longToast(final Context context, final int msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Deprecated
    public static void crash(final String msg) {
        throw new RuntimeException(msg);
    }

}
