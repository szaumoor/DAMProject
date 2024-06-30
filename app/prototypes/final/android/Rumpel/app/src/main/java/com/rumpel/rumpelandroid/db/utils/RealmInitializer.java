package com.rumpel.rumpelandroid.db.utils;

import android.content.Context;
import android.util.Log;

import com.rumpel.rumpelandroid.R;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoDatabase;

/**
 * Utility class that takes care of the Realm initialization that is required for database access in the app
 */
public final class RealmInitializer {
    private static boolean realmCalled;
    private static MongoDatabase database;
    private static final String DB_NAME = "rumpel";
    /**
     * Private constructor to prevent instantiation
     */
    private RealmInitializer() {
        throw new AssertionError();
    }

    /**
     * Initializes Realm to allow database access
     *
     * @param context Context from which to initialize Realm
     * @param runnable Runnable to be executed after initialization
     */
    public static void init(final Context context, final Runnable runnable) {
        if (!realmCalled) {
            Realm.init(context);
            App app = new App(new AppConfiguration.Builder(context.getString(R.string.realm_app_id)).build());
            User login = app.login(Credentials.anonymous());
            if (login.isLoggedIn()) {
                Log.v("MongoDB", "Anonymous Login is successful");
                realmCalled = true;
                var client = login.getMongoClient(context.getString(R.string.realm_data_source_name));
                database = client.getDatabase(DB_NAME);
                runnable.run();
            } else Log.v("MongoDB", "Anonymous login failed");
        } else runnable.run();
    }

    /**
     * Getter for the database object
     *
     * @return the MongoDatabase object
     */
    public static MongoDatabase getDatabase() {
        return database;
    }
}
