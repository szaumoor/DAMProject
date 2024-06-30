package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.fragments.AddPaymentMethodFragment;
import com.rumpel.rumpelandroid.activities.fragments.AddTagFragment;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorTag;
import com.rumpel.rumpelandroid.realm.DAOPaymentMethods;
import com.rumpel.rumpelandroid.realm.DAOTags;
import com.rumpel.rumpelandroid.realm.DAOUser;
import com.rumpel.rumpelandroid.realm.Outcome;
import com.rumpel.rumpelandroid.utils.AndroidUtils;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;

import java.util.List;

public class TagActivity extends AppCompatActivity implements InjectorTag {
    private List<Tag> tags;
    private Spinner spinner;
    private DAOTags dao;

    //private static CachedList<PaymentMethod> cachedList; // to reduce database calls and improve performance -- todo later, might cause sync problems though

    private void updateSpinner() {
        ArrayAdapter<Tag> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tags);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        dao = new DAOTags(this);
        tags = dao.getAll();
        ArrayAdapter<Tag> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tags);
        spinner = findViewById(R.id.spinner_tags_id);
        spinner.setAdapter(adapter);
        FloatingActionButton button = findViewById(R.id.float_btn);
        button.setOnClickListener(view -> new AddTagFragment().show(getSupportFragmentManager(), null));
    }


    @Override
    public void addTag(String name) {
        if (name == null) crash("Cannot add a payment method with a null name");
        var pm = Tag.of(name);
        if (pm.isPresent()) {
            Tag tag = pm.get();
            tag.setUser(DAOUser.getLoggedUser());
            Outcome out = dao.insert(tag);
            if (out == Outcome.SUCCESS) {
                tags.add(tag);
                updateSpinner();
            } else if (out == Outcome.UNIQUE_EXISTS) AndroidUtils.toast(this, "Pm already exists");
            else crash("Error inserting payment method in the database!");
        } else crash("Error creating the payment method");
    }

    @Override
    public void modifyTag(String name) {

    }
}