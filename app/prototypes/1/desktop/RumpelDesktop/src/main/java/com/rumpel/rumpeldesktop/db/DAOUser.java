package com.rumpel.rumpeldesktop.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.interfaces.UserPrimaryField;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.Username;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.*;

public class DAOUser extends DAO<UserPrimaryField, User> {

    private final MongoCollection<Document> collection;
    private static User loggedUser;

    public static final String USER_ID_FIELD = "_id";
    public static final String COLL_USERS = "users";
    public static final String USERNAME_FIELD = "username";
    public static final String EMAIL_FIELD = "email";
    public static final String PASS_FIELD = "password";

    public DAOUser() {
        collection = database.getCollection(COLL_USERS);
    }

    @Override
    public Optional<User> get(final UserPrimaryField id) {
        if (Objects.isNull(id)) return Optional.empty();
        if (id instanceof Username username) {
            Document first = collection.find(Filters.eq(USERNAME_FIELD, username.get())).first();
            return Documents.parseUser(first);
        } else if (id instanceof UserEmail email) {
            Document first = collection.find(Filters.eq(EMAIL_FIELD, email.get())).first();
            return Documents.parseUser(first);
        } else return Optional.empty();
    }

    public Optional<User> authenticate(final UserPrimaryField id, final char[] pass) {
        final Optional<User> user = get(id);
        if (user.isEmpty()) return Optional.empty();
        boolean verify = user.get().getUserPass().verify(String.valueOf(pass));
        if (!verify) return Optional.empty();
        else {
            loggedUser = user.get();
            logger.info("user " + loggedUser.getUsername().get() + " authenticated at local date: " + LocalDateTime.now());
            return user;
        }
    }

    @Override
    public List<User> getAll() {
        logger.info("Attempting to find all user documents");
        return collection.find()
                .map(Documents::parseUser)
                .into(new ArrayList<>())
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Outcome insert(final User user) {
        if (Objects.isNull(user)) return Outcome.NULL;
        if (userExists(user)) return Outcome.UNIQUE_EXISTS;
        var op = Documents.userToDocument(user);
        var present = op.isPresent();
        if (present) {
            collection.insertOne(op.get());
            return Outcome.SUCCESS;
        }
        return Outcome.ERROR;
    }

    private boolean userExists(final User user) {
        return get(user.getUsername()).isPresent() ||
                get(user.getEmail()).isPresent();
    }

    public static Optional<User> getLoggedUser() {
        return Optional.ofNullable(loggedUser);
    }

    public void logout() {
        loggedUser = null;
    }
}
