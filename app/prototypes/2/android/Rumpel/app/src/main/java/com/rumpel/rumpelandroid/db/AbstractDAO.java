package com.rumpel.rumpelandroid.db;

import android.content.Context;
import android.util.Log;

import com.rumpel.rumpelandroid.db.utils.RealmInitializer;

import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.realm.mongodb.mongo.MongoCollection;

public abstract class AbstractDAO {

    protected MongoCollection<Document> coll;

    protected AbstractDAO(final Context context, final String collName) {
        var executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> RealmInitializer.init(context, () -> {
            coll = RealmInitializer.getDatabase().getCollection(collName);
            Log.v("DAO", "Collection retrieved");
        }));
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(5, TimeUnit.SECONDS);
            if (!b) Log.e("DAO", "Failed to connect to Mongo in time");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
