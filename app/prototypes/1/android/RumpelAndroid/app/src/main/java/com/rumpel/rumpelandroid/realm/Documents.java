package com.rumpel.rumpelandroid.realm;

import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.User;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Optional;

public enum Documents {
    ;

    public static Optional<Document> userToDocument(final User user) {
        if (Objects.isNull(user)) return Optional.empty();

        final var username = user.getUsername().get();
        final var email = user.getEmail().get();
        final var password = user.getUserPass().getHashedPass();

        final Document doc = new Document();
        doc.put(DAOUser.USERNAME_FIELD, username);
        doc.put(DAOUser.EMAIL_FIELD, email);
        doc.put(DAOUser.PASS_FIELD, password);

        System.out.println(doc);

        return Optional.of(doc);
    }

    public static Optional<Document> tagToDocument(final Tag tag) {
        if (Objects.isNull(tag)) return Optional.empty();

        final var name = tag.getName();
        final var userId = (ObjectId) tag.getUser().getId();

        final Document doc = new Document();
        doc.put(DAOTags.NAME_FIELD, name);
        doc.put(DAOTags.USER_ID_FIELD, userId);

        return Optional.of(doc);
    }

    public static Tag toTag(final Document doc) {
        var name = doc.getString(DAOTags.NAME_FIELD);
        Tag tag = new Tag(name);
        tag.setUser(DAOUser.getLoggedUser());
        return tag;
    }

    public static Optional<Document> paymentMethodToDocument(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Optional.empty();

        final var name = pm.getName();
        final var userId = (ObjectId) pm.getUser().getId();

        final Document doc = new Document();
        doc.put(DAOPaymentMethods.NAME_FIELD, name);
        doc.put(DAOPaymentMethods.USER_ID_FIELD, userId);

        System.out.println(doc);

        return Optional.of(doc);
    }

    public static PaymentMethod toPaymentMethod(final Document doc) {
        var name = doc.getString(DAOPaymentMethods.NAME_FIELD);
        PaymentMethod pm = new PaymentMethod(name);
        pm.setUser(DAOUser.getLoggedUser());
        return pm;
    }
}
