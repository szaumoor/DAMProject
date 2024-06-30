package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.fragments.AddBudgetFragment;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorBudget;
import com.szaumoor.rumple.model.entities.Budget;

public class BudgetActivity extends AppCompatActivity implements InjectorBudget, DatePickerDialog.OnDateSetListener {

    private RecyclerView recycler;
    private Budget selectedBudget;
    private AddBudgetFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        recycler = findViewById(R.id.recycler);

    }

    @Override
    public void apply(final Budget budget) {
        if (budget == null) crash("Budget should not be null");
        selectedBudget = budget;
        frag = new AddBudgetFragment();
        frag.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}