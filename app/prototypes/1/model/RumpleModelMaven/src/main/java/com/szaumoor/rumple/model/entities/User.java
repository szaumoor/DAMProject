package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.model.utils.Types;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.UserPass;
import com.szaumoor.rumple.model.utils.types.Username;

import java.util.Objects;
import java.util.Optional;

public final class User extends AbstractEntity<Object> {

    private Username username;
    private UserEmail email;
    private UserPass userPass;

    /**
     * Constructor for a user. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param username The username of this user
     * @param email The email of this user
     * @param userPass The password of this user (hash)
     */
    public User(final Username username, final UserEmail email, final UserPass userPass) {
        this.username = username;
        this.email = email;
        this.userPass = userPass;
    }

    public static Optional<User> of(final Username username, final UserEmail email, final UserPass userPass) {
        if (Types.anyNull(username, email, userPass)) return Optional.empty();
        return Optional.of(new User(username, email, userPass));
    }

    public Username getUsername() {
        return username;
    }

    public UserEmail getEmail() {
        return email;
    }

    public UserPass getUserPass() {
        return userPass;
    }

    public boolean setUsername(final Username username) {
        final boolean valid = Objects.nonNull(username);
        if (valid) this.username = username;
        return valid;
    }

    public boolean setEmail(final UserEmail email) {
        final boolean valid = Objects.nonNull(email);
        if (valid) this.email = email;
        return valid;
    }

    public boolean setUserPass(final UserPass newPass) {
        final boolean valid = Objects.nonNull(newPass);
        if (valid) this.userPass = newPass;
        return valid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), username, email);
    }

}
