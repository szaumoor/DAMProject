package com.rumpel.rumpelandroid.realm;

import static com.rumpel.rumpelandroid.realm.Filters.eq;

import android.content.Context;

import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.Strings;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.UserPass;
import com.szaumoor.rumple.model.utils.types.Username;

import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;

public class DAOUser extends AbstractBaseDAO<User> {

    public static final String USERS_COLL = "users";

    public static final String ID_FIELD = "_id";
    public static final String USERNAME_FIELD = "username";
    public static final String EMAIL_FIELD = "email";
    public static final String PASS_FIELD = "password";

    private static final String CLASS_TAG = DAOUser.class.getSimpleName();
    private static User loggedUser;

    public DAOUser(final Context context) {
        super(context, USERS_COLL);
    }

    @Override
    public Optional<User> get(final Object id) {
        final var executor = Executors.newSingleThreadExecutor();
        final AtomicReference<User> ref = new AtomicReference<>();
        executor.submit(() -> {
            final Document doc = coll.findOne(eq(USERNAME_FIELD, id.toString())).get();
            User user = new User(new Username(doc.getString(USERNAME_FIELD)),
                                 new UserEmail(doc.getString(EMAIL_FIELD)),
                                 new UserPass(doc.getString(PASS_FIELD))
            );
            user.setId(doc.getObjectId(ID_FIELD));
            ref.set(user);
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return Optional.ofNullable(ref.get());
        } catch (InterruptedException e) {
            Logs.interrupt(CLASS_TAG);
            throw new RuntimeException(e);
        }
    }


    @Override
    public Outcome insert(final User obj) {
        if (Objects.isNull(obj)) throw new NullPointerException();
        final Document doc = Documents.userToDocument(obj).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<User> user = get(obj.getUsername().get());
        if (user.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> coll.insertOne(doc).get());
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) return Outcome.SUCCESS;
            else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            Logs.interrupt(CLASS_TAG);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Outcome modify(final User entity) {
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
