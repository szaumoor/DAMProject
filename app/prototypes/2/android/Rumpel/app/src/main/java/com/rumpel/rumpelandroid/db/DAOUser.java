package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;
import static com.szaumoor.rumple.model.entities.User.EMAIL_FIELD;
import static com.szaumoor.rumple.model.entities.User.PASS_FIELD;
import static com.szaumoor.rumple.model.entities.User.USERNAME_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.rumpel.rumpelandroid.utils.Logs;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.UserPass;
import com.szaumoor.rumple.model.entities.types.Username;
import com.szaumoor.rumple.utils.Strings;

import org.bson.Document;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.mongo.result.InsertOneResult;

public class DAOUser extends AbstractBaseDAO<User> {

    private static User loggedUser;

    public DAOUser(final Context context) {
        super(context, User.COLL_USER);
    }

    @Override
    public Optional<User> get(final Object id) {
        return crudGet(eq(USERNAME_FIELD, id.toString()), Documents::toUser, true);
    }

    @Override
    public Optional<User> getById(Object o) {
        return Optional.empty();
    }


    @Override
    public Outcome insert(final User obj) {
        if (Objects.isNull(obj)) throw new NullPointerException();
        final Document doc = Documents.userToDocument(obj).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<User> user = get(obj.getUsername().username());
        if (user.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> {
            var insertOneResult = coll.insertOne(doc).get();
            obj.setId(insertOneResult.getInsertedId());
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) return Outcome.SUCCESS;
            else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Outcome modify(final User entity) {
        return null;
    }

    @Override
    public Outcome delete(User user) {
        return null;
    }

    public boolean authenticate(final String username, final String password) {
        if (!Strings.hasContent(username) || !Strings.hasContent(password)) return false;
        var op = get(username);
        if (op.isEmpty()) return false;
        loggedUser = op.get();
        return loggedUser.getUserPass().verify(password);
    }

    public static void logout(){
        loggedUser = null;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }
}
