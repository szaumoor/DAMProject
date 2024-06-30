package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.DateUtils.formatDate;
import static com.rumpel.rumpelandroid.utils.NumUtils.formatCurrency;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.fragments.AboutDialogFragment;
import com.rumpel.rumpelandroid.realm.DAOUser;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.PaymentMethod;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private GridView gridView;
    private TextView totalBillExpenses;
    private List<Bill> bills;

    public void init() {
        drawerLayout = findViewById(R.id.drawerLayout);
        FloatingActionButton floatingButton = findViewById(R.id.float_btn);
        floatingButton.setOnClickListener(view -> startActivity(new Intent(this, AddBillActivity.class)));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(formatDate(ZonedDateTime.now()));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_id);
        navView.setNavigationItemSelectedListener(item -> {
            final int id = item.getItemId();
            if (id == R.id.nav_menu_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_menu_payment_method){
                startActivity(new Intent(this, PaymentMethodActivity.class));
            } else if (id == R.id.nav_menu_budgets) {
                startActivity(new Intent(this, BudgetActivity.class));
            } else if (id == R.id.nav_menu_tags) {
                startActivity(new Intent(this, TagActivity.class));
            } else if (id == R.id.nav_menu_about) {
                new AboutDialogFragment().show(getSupportFragmentManager(), null);
            } else if (id == R.id.nav_menu_logout) {
                DAOUser.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });
        bills = new ArrayList<>(20);
        totalBillExpenses = findViewById(R.id.txt_view_bill_total);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        // connect db
        gridView = findViewById(R.id.grid_bills_id);
        testGrid();
        String total = getString(R.string.total) + " " +
                formatCurrency(
                    bills.stream()
                            .map(Bill::getTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
        totalBillExpenses.setText(total);
    }

    private void testGrid() {
        Currency curr = Currency.getInstance(Locale.getDefault());
        for (int i = 1; i <= 20; i++) {

            ItemBill i1 = ItemBill.of("null", new BigDecimal("1").multiply(BigDecimal.valueOf(i)), null).get();
            ItemBill i2 = ItemBill.of("null", new BigDecimal("2").multiply(BigDecimal.valueOf(i)), null).get();
            ItemBill i3 = ItemBill.of("null", new BigDecimal("3").multiply(BigDecimal.valueOf(i)), null).get();
            ItemBill i4 = ItemBill.of("null", new BigDecimal("4").multiply(BigDecimal.valueOf(i)), null).get();
            ItemBill i5 = ItemBill.of("null", new BigDecimal("5").multiply(BigDecimal.valueOf(i)), null).get();
            Bill bill = Bill.of(curr, new PaymentMethod("Cash"), List.of(i1, i2, i3, i4, i5)).get();
            bill.calcTotal();
            bills.add(bill);
        }

        BillGridAdapter adapter = new BillGridAdapter(this, R.layout.grid_item_layout, bills);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
         else super.onBackPressed();
    }
}