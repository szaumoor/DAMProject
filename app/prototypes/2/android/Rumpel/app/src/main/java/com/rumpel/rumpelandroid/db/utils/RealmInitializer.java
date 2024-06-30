package com.rumpel.rumpelandroid.db.utils;

import android.content.Context;
import android.util.Log;

import com.rumpel.rumpelandroid.R;
import com.szaumoor.rumple.db.utils.Procedure;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public enum RealmInitializer {
    ;
    private static boolean realmCalled;
    private static App app;
    private static MongoDatabase database;

    public static void init(final Context context, Procedure procedure) {
        if (!realmCalled) {
            Realm.init(context);
            app = new App(new AppConfiguration.Builder(context.getString(R.string.realm_app_id)).build());
            User login = app.login(Credentials.anonymous());
            if (login.isLoggedIn()) {
                Log.v("MONGO", "Anonymous Login is successful");
                realmCalled = true;
                MongoClient client = app.currentUser().getMongoClient(context.getString(R.string.realm_data_source_name));
                database = client.getDatabase("rumpel");
                procedure.execute();
            } else Log.v("MONGO", "Anonymous login failed");
        } else procedure.execute();
    }

    public static App getApp() {
        return app;
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
