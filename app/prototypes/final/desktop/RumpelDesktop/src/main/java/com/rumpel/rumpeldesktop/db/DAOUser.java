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

/**
 * DAO class that handles User entities and objects.
 */
public class DAOUser extends AbstractBaseDAO<User> {

    private static User loggedUser;

    /**
     * No-args constructor initializing the basic fields and retrieves the collection.
     */
    public DAOUser() {
        super(User.COLL_USER);
        logger.info("dao user called");
    }

    /**
     * Retrieves an optional User object based on the given id.
     *
     * @param id the id of the User object to retrieve
     * @return an optional User object if found, otherwise empty
     */
    @Override
    public Optional<User> get(final Object id) {
        if (isNull(id)) return Optional.empty();
        if (id instanceof Username username) {
            Document first = collection.find(eq(USERNAME_FIELD, username.value())).first();
            return Documents.parseUser(first);
        } else if (id instanceof UserEmail email) {
            Document first = collection.find(eq(EMAIL_FIELD, email.value())).first();
            return Documents.parseUser(first);
        } else return Optional.empty();
    }

    /**
     * Authenticates a user based on the provided ID and password.
     *
     * @param id   the primary field of the user
     * @param pass the password of the user
     * @return an optional containing the authenticated user if successful, otherwise an empty optional
     */
    public Optional<User> authenticate(final UserPrimaryField id, final char[] pass) {
        final Optional<User> user = get(id);
        if (user.isEmpty()) return Optional.empty();
        var verify = user.get().getUserPass().verify(String.valueOf(pass));
        if (!verify) return Optional.empty();
        else {
            loggedUser = user.get();
            logger.info("user " + loggedUser.getUsername().value() + " authenticated at local date: " + LocalDateTime.now());
            return user;
        }
    }

    /**
     * Inserts a user into the collection.
     *
     * @param user the user to be inserted
     * @return the outcome of the insert operation
     */
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

    /**
     * Modify the user.
     *
     * @param user the user to be modified
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final User user) {
        return crudModify(user, null, Documents::userToDocument);
    }

    /**
     * Deletes a user and all associated data, including payment methods, tags, budgets, and bills.
     *
     * @param user the user to be deleted
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome delete(final User user) {
        Outcome outcome = new DAOPaymentMethod().deleteAll();
        if (outcome == Outcome.ERROR) {
            logger.error("Failed to delete payment methods");
            return outcome;
        }
        logger.info("payment methods from user " + user.getUsername().value() + " deleted");
        Outcome outcome1 = new DAOTag().deleteAll();
        if (outcome1 == Outcome.ERROR) {
            logger.error("Failed to delete tags");
            return outcome1;
        }
        logger.info("tags from user " + user.getUsername().value() + " deleted");
        Outcome outcome2 = new DAOBudget().deleteAll();
        if (outcome2 == Outcome.ERROR) {
            logger.error("Failed to delete budgets");
            return outcome2;
        }
        logger.info("budgets from user " + user.getUsername().value() + " deleted");
        Outcome outcome3 = new DAOBill().deleteAll();
        if (outcome3 == Outcome.ERROR) {
            logger.error("Failed to delete bills");
            return outcome3;
        }
        logger.info("bills from user " + user.getUsername().value() + " deleted");
        return crudDelete(user, Documents::userToDocument);
    }

    /**
     * Checks if a user already exists in the system, based on the username or email.
     *
     * @param user the user object to check for existence
     * @return true if the user exists, false otherwise
     */
    private boolean userExists(final User user) {
        return get(user.getUsername()).isPresent() ||
                get(user.getEmail()).isPresent();
    }

    /**
     * Retrieves the logged-in user.
     *
     * @return the logged-in user
     */
    public static User getLoggedUser() {
        return loggedUser;
    }

    /**
     * Logs out the user by setting the loggedUser variable to null.
     * In the context of the app, a user is considered logged in when the loggedUser variable is not null.
     */
    public static void logout() {
        loggedUser = null;
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return an optional containing the user if found, or an empty optional if not found
     */
    @Override
    public Optional<User> getById(final Object id) {
        throw new UnsupportedOperationException();
    }
}
