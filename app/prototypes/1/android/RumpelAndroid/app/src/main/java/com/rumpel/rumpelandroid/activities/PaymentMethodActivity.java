package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.fragments.AddPaymentMethodFragment;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorPaymentMethod;
import com.rumpel.rumpelandroid.realm.DAOPaymentMethods;
import com.rumpel.rumpelandroid.realm.DAOUser;
import com.rumpel.rumpelandroid.realm.Outcome;
import com.rumpel.rumpelandroid.realm.cache.CachedList;
import com.rumpel.rumpelandroid.utils.AndroidUtils;
import com.szaumoor.rumple.model.entities.PaymentMethod;

import java.util.List;

public class PaymentMethodActivity extends AppCompatActivity implements InjectorPaymentMethod {
    private List<PaymentMethod> paymentMethods;
    private Spinner spinner;
    private DAOPaymentMethods dao;

    //private static CachedList<PaymentMethod> cachedList; // to reduce database calls and improve performance -- todo later, might cause sync problems though

    private void updateSpinner() {
        ArrayAdapter<PaymentMethod> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        dao = new DAOPaymentMethods(this);
        paymentMethods = dao.getAll();
        ArrayAdapter<PaymentMethod> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        spinner = findViewById(R.id.spinner_pm_id);
        spinner.setAdapter(adapter);
        FloatingActionButton button = findViewById(R.id.float_btn);
        button.setOnClickListener(view -> new AddPaymentMethodFragment().show(getSupportFragmentManager(), null));
    }

    @Override
    public void addPaymentMethod(final String name) {
        if (name == null) crash("Cannot add a payment method with a null name");
        var pm = PaymentMethod.of(name);
        if (pm.isPresent()) {
            PaymentMethod paymentMethod = pm.get();
            paymentMethod.setUser(DAOUser.getLoggedUser());
            Outcome out = dao.insert(paymentMethod);
            if (out == Outcome.SUCCESS) {
                paymentMethods.add(paymentMethod);
                updateSpinner();
            } else if (out == Outcome.UNIQUE_EXISTS) AndroidUtils.toast(this, "Pm already exists");
            else crash("Error inserting payment method in the database!");
        } else crash("Error creating the payment method");
    }

    @Override
    public void modifyPaymentMethod(final String name) {

    }
}