package com.rumpel.rumpelandroid.realm;

import static com.rumpel.rumpelandroid.realm.Filters.and;
import static com.rumpel.rumpelandroid.realm.Filters.eq;

import android.content.Context;

import com.szaumoor.rumple.model.entities.PaymentMethod;

import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DAOPaymentMethods extends AbstractPluralDAO<PaymentMethod> {

    public static final String COLL_PM = "payment_methods";
    public static final String NAME_FIELD = "name";
    public static final String USER_ID_FIELD = "user_id";

     public DAOPaymentMethods(final Context context) {
        super(context, COLL_PM);
    }

    @Override
    public Optional<PaymentMethod> get(Object id) {
        final var executor = Executors.newSingleThreadExecutor();
        final AtomicReference<PaymentMethod> ref = new AtomicReference<>();
        executor.submit(() -> {
            final Document doc = coll.findOne(and(
                    eq(NAME_FIELD, id.toString()),
                    eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()))).get();
            PaymentMethod method = Documents.toPaymentMethod(doc);
            ref.set(method);
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return Optional.ofNullable(ref.get());
        } catch (InterruptedException e) {
            Logs.interrupt(getClass().getSimpleName());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Outcome insert(final PaymentMethod entity) {
        if (Objects.isNull(entity)) throw new NullPointerException();
        final Document doc = Documents.paymentMethodToDocument(entity).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<PaymentMethod> pm = get(entity.getName());
        if (pm.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> coll.insertOne(doc).get());
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
    public Outcome modify(PaymentMethod entity) {
        return null;
    }

    @Override
    public List<PaymentMethod> getAll() {
        return AbstractPluralDAO.crudGetAll(coll, USER_ID_FIELD, Documents::toPaymentMethod);
    }

    @Override
    public Outcome insertAll(List<PaymentMethod> elements) {
        return null;
    }
}
