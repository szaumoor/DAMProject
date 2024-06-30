package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;
import static com.szaumoor.rumple.model.entities.User.EMAIL_FIELD;
import static com.szaumoor.rumple.model.entities.User.USERNAME_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;
import static com.szaumoor.utils.Strings.hasContent;

import static java.util.Objects.isNull;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.Username;
import com.szaumoor.rumple.model.interfaces.UserPrimaryField;

import org.bson.Document;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DAO class that handles User entities and objects.
 */
public class DAOUser extends AbstractBaseDAO<User> {

    private static User loggedUser;

    /**
     * Constructor that initializes the basic fields and retrieves the collection.
     */
    public DAOUser(final Context context) {
        super(context, User.COLL_USER);
    }

    /**
     * Retrieves an optional User object based on the given id.
     *
     * @param id the id of the User object to retrieve
     * @return an optional User object if found, otherwise empty
     */
    @Override
    public Optional<User> get(final Object id) {
        if (id instanceof Username username){
            return crudGet(eq(USERNAME_FIELD, username.value()), Documents::parseUser, true);
        } else if (id instanceof UserEmail email) {
            return crudGet(eq(EMAIL_FIELD, email.value()), Documents::parseUser, true);
        } else return Optional.empty();
    }
    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return an optional containing the user if found, or an empty optional if not found
     */
    @Override
    public Optional<User> getById(final Object id) {
        return crudGet(eq(ID_FIELD, id), Documents::parseUser, true);
    }

    /**
     * Inserts a user into the collection.
     *
     * @param user the user to be inserted
     * @return the outcome of the insert operation
     */
    @Override
    public Outcome insert(final User user) {
        if (isNull(user)) throw new NullPointerException();
        final Document doc = Documents.userToDocument(user).orElseThrow();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<User> op = get(user.getUsername().value());
        if (op.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> {
            var insertOneResult = collection.insertOne(doc).get();
            user.setId(insertOneResult.getInsertedId());
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
    /**
     * Modify the user.
     *
     * @param user the user to be modified
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final User user) {
        return crudModify(user, Documents::userToDocument);
    }

    @Override // currently unsupported
    public Outcome delete(final User user) {
        throw new UnsupportedOperationException();
    }

    /**
     * Authenticates a user based on the provided ID and password.
     *
     * @param id the id of the user
     * @param password the password of the user
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(final UserPrimaryField id, final String password) {
        if (isNull(id) || !hasContent(password)) return false;
        var op = get(id);
        if (op.isEmpty()) return false;
        loggedUser = op.get();
        boolean verify = loggedUser.getUserPass().verify(password);
        if (!verify) logout();
        return verify;
    }

    /**
     * Logs the user out, which in the context of this application means setting the logged user to null.
     */
    public static void logout() {
        loggedUser = null;
    }

    /**
     * Getter for the currently logged user.
     *
     * @return the currently logged user
     */
    public static User getLoggedUser() {
        return loggedUser;
    }
}
