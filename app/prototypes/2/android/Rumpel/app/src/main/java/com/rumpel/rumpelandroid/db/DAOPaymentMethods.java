package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.rumpel.rumpelandroid.utils.Logs;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.interfaces.Entity;

import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.mongo.result.InsertOneResult;

public class DAOPaymentMethods extends AbstractPluralDAO<PaymentMethod> {

    public DAOPaymentMethods(final Context context) {
        super(context, PaymentMethod.COLL_PMS);
    }

    @Override
    public Optional<PaymentMethod> get(final Object id) {
        final var executor = Executors.newSingleThreadExecutor();
        final AtomicReference<PaymentMethod> ref = new AtomicReference<>();
        executor.submit(() -> {
            final Document doc = coll.findOne(and(
                    eq(PaymentMethod.NAME_FIELD, id.toString()),
                    eq(Entity.USER_ID_FIELD, DAOUser.getLoggedUser().getId()))).get();
            ref.set(Documents.toPaymentMethod(doc));
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return Optional.ofNullable(ref.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PaymentMethod> getById(Object o) {
        return crudGet(eq(Entity.ID_FIELD, o), Documents::toPaymentMethod, false);
    }

    @Override
    public Outcome insert(final PaymentMethod entity) {
        if (Objects.isNull(entity)) throw new NullPointerException();
        final Document doc = Documents.paymentMethodToDocument(entity).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<PaymentMethod> pm = get(entity.getName());
        if (pm.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> {
            InsertOneResult insertOneResult = coll.insertOne(doc).get();
            entity.setId(insertOneResult.getInsertedId());
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) return Outcome.SUCCESS;
            else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            Logs.interrupt(getClass().getSimpleName());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Outcome delete(PaymentMethod paymentMethod) {
        return null;
    }

    @Override
    public List<PaymentMethod> getAll() {
        return crudGetAll(Documents::toPaymentMethod);
    }

    @Override
    public List<PaymentMethod> getAllById(List<Object> list) {
        return null;
    }

    @Override
    public Outcome insertAll(List<PaymentMethod> elements) {
        return null;
    }

    @Override
    public Outcome deleteAll(List<PaymentMethod> list) {
        return null;
    }

    @Override
    public Outcome deleteAll() {
        return null;
    }

    @Override
    public Outcome modify(final PaymentMethod pm) {
        if (Objects.isNull(pm)) return Outcome.NULL;
        final var executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
                    coll.updateOne(
                    and(
                            eq(Entity.ID_FIELD, pm.getId()),
                            eq(Entity.USER_ID_FIELD, DAOUser.getLoggedUser().getId())
                    ),
                    Documents.paymentMethodToDocument(pm).orElseThrow()).get();
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            return b ? Outcome.SUCCESS : Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
