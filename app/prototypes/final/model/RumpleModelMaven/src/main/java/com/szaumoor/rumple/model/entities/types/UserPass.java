package com.szaumoor.rumple.model.entities.types;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import com.password4j.Password;
import com.password4j.SecureString;
import com.szaumoor.utils.Strings;

/**
 * Class that encapsulates a user password and handles password hashing and checking using the BCrypt algorithm.
 */
public final class UserPass {
    private final String hashedPass;

    public static final int MIN_SIZE = 8;
    public static final int MAX_SIZE = 128;


    /**
     * Constructor for the user password. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param hashedPass The password hash to which plain-text passwords need to be validated.
     */
    public UserPass(final String hashedPass) {
        this.hashedPass = hashedPass;
    }

    /**
     * Private constructor where the hashing is done using the BCrypt algorithm. To properly create a hashed password,
     * the of() method should be used.
     *
     * @param password The password to be hashed.
     */
    private UserPass(final SecureString password) {
        this.hashedPass = Password.hash(password)
                .withBcrypt()
                .getResult();
    }

    /**
     * Static method to create an optional containing nothing if invalid or a new object of this class if valid.
     * For a password to be valid, it must have between 8 and 128 characters.
     *
     * @param password A char array of the unencrypted chosen password. Must not be null or outside the range (8-128) in length.
     * @return An Optional that is empty if the aforementioned conditions are not met, or containing a properly created object of this class
     * storing the password hashed using the BCrypt algorithm.
     */
    public static Optional<UserPass> of(final char[] password) {
        if (!validate(password)) return Optional.empty();
        var userPass = Optional.of(new UserPass(new SecureString(password)));
        Arrays.fill(password, '*');
        return userPass;
    }

    /**
     * Checks whether a plain password is valid or not. In this context, it means that it has between 8 and 128 characters.
     * A null char array passed as argument will also make this method return false.
     *
     * @param password The char array to check
     * @return True if valid, false otherwise
     */
    public static boolean validate(final char [] password) {
        return Objects.nonNull(password) && Strings.between(password, MIN_SIZE, MAX_SIZE);
    }

    /**
     * Retrieves the hashed password.
     *
     * @return the hashed password.
     */
    public String getHashedPass() {
        return hashedPass;
    }

    /**
     * Verifies that a plain password matches against
     *
     * @param password The plain text password to verify against this object's stored hash
     * @return True is verification was possible, false otherwise
     */
    public boolean verify(final String password) {
        return Password.check(password.getBytes(), hashedPass.getBytes()).withBcrypt();
    }

    /**
     * Overridden equals() method. For this class, equality is defined as having the same hashed password or being the same
     * object in memory.
     *
     * @param o The object to compare against
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPass userPass = (UserPass) o;
        return Objects.equals(hashedPass, userPass.hashedPass);
    }

    /**
     * Retrieves the hash code value for this object. The hash code is generated
     * based on the hashedPass property of the object.
     *
     * @return  the hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(hashedPass);
    }
}
