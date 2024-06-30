package com.rumpel.rumpelandroid.db;

import android.content.Context;
import android.util.Log;

import com.rumpel.rumpelandroid.db.utils.RealmInitializer;

import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.realm.mongodb.mongo.MongoCollection;

/**
 * Class that handles the connection to the database.
 */
public abstract class AbstractDAO {

    protected MongoCollection<Document> collection;
    /**
     * Base constructor all DAOs, attempts to connect to MongoDB if connection wasn't established previously.
     *
     * @param context  Context of the application
     * @param collName Collection name for retrieval
     */
    protected AbstractDAO(final Context context, final String collName) {
        var executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> RealmInitializer.init(context, () -> {
            collection = RealmInitializer.getDatabase().getCollection(collName);
            Log.v("DAO", "Collection " + collName +  " retrieved");
        }));
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(20, TimeUnit.SECONDS);
            if (!b) Log.e("DAO", "Failed to connect to MongoDB in time");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
