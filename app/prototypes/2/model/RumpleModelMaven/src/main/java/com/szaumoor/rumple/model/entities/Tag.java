package com.szaumoor.rumple.model.entities;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.utils.Strings;

import java.util.Objects;
import java.util.Optional;

/**
 * This class encapsulates a tag in Rumpel.
 */
public final class Tag extends AbstractEntity<Object> {

    public static final String COLL_TAGS = "tags";
    public static final String NAME_FIELD = "name";

    private String name;
    private User user;

    /**
     * Constructor for a tag. Does no validation of any sort and should only be used to construct objects
     * when it's certain that they are valid, such as when pulling them from a database where insertions were previously
     * validated.
     *
     * @param name The name of this tag.
     */
    public Tag(final String name) {
        this.name = name;
    }

    public Tag(final Tag tag) {
        super(tag.getId());
        this.name = tag.name;
        this.user = tag.user;
    }

    public Tag(final String name, final User user) {
        this(name);
        this.user = user;
    }

    public static Optional<Tag> of(final String name) {
        if (!Strings.hasContent(name)) return Optional.empty();
        return Optional.of(new Tag(name));
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public boolean setName(final String name) {
        final boolean valid = Strings.hasContent(name);
        if (valid) this.name = name;
        return valid;
    }

    public boolean setUser(final User user) {
        final boolean valid = Objects.nonNull(user);
        if (valid) this.user = user;
        return valid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) && Objects.equals(user, tag.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user);
    }

    @Override
    public String toString() {
        return name;
    }
}
