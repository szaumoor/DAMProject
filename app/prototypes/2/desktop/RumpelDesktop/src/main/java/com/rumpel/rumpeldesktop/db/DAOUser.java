package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.Username;
import com.szaumoor.rumple.model.interfaces.UserPrimaryField;

import org.bson.Document;

import java.time.LocalDateTime;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.szaumoor.rumple.model.entities.User.EMAIL_FIELD;
import static com.szaumoor.rumple.model.entities.User.USERNAME_FIELD;
import static java.util.Objects.isNull;

public class DAOUser extends AbstractBaseDAO<User> {

    private static User loggedUser;

    public DAOUser() {
       super(User.COLL_USER);
       logger.info("dao user called");
    }

    @Override
    public Optional<User> get(final Object id) {
        if (isNull(id)) return Optional.empty();
        if (id instanceof Username username) {
            Document first = collection.find(eq(USERNAME_FIELD, username.username())).first();
            return Documents.parseUser(first);
        } else if (id instanceof UserEmail email) {
            Document first = collection.find(eq(EMAIL_FIELD, email.email())).first();
            return Documents.parseUser(first);
        } else return Optional.empty();
    }

    public Optional<User> authenticate(final UserPrimaryField id, final char[] pass) {
        final Optional<User> user = get(id);
        if (user.isEmpty()) return Optional.empty();
        var verify = user.get().getUserPass().verify(String.valueOf(pass));
        if (!verify) return Optional.empty();
        else {
            loggedUser = user.get();
            logger.info("user " + loggedUser.getUsername().username() + " authenticated at local date: " + LocalDateTime.now());
            return user;
        }
    }

    @Override
    public Outcome insert(final User user) {
        if (isNull(user)) return Outcome.NULL;
        if (userExists(user)) return Outcome.UNIQUE_EXISTS;
        var op = Documents.userToDocument(user);
        var present = op.isPresent();
        if (present) {
            collection.insertOne(op.get());
            return Outcome.SUCCESS;
        }
        return Outcome.ERROR;
    }

    @Override
    public Outcome modify(final User user) {
        return crudModify(user, null, Documents::userToDocument);
    }

    @Override
    public Outcome delete(User user) {
        return null;
    }

    private boolean userExists(final User user) {
        return get(user.getUsername()).isPresent() ||
                get(user.getEmail()).isPresent();
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void logout() {
        loggedUser = null;
    }

    @Override
    public Optional<User> getById(Object id) {
        return Optional.empty();
    }
}
