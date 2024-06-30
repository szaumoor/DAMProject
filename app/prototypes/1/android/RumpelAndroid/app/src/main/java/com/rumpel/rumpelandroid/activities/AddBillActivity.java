package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;
import static com.rumpel.rumpelandroid.utils.AndroidUtils.toast;
import static com.rumpel.rumpelandroid.utils.NumUtils.formatCurrency;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.fragments.AddItemBillFragment;
import com.rumpel.rumpelandroid.activities.fragments.DatePickerFragment;
import com.rumpel.rumpelandroid.activities.fragments.UpdateItemDialogFragment;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorBill;
import com.rumpel.rumpelandroid.activities.interfaces.Updater;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AddBillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, InjectorBill, Updater {
    private DatePickerFragment datePicker;
    private AddItemBillFragment itemBillFrag;
    private UpdateItemDialogFragment updateFrag;

    private Bill bill;
    private ItemBill selectedItem;

    private TextView txtDate;
    private TextView txtTotal;
    private ListView listView;
    private Button btnAddBill;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final int CAMERA_START_CODE = 1;

    private void takePicture() {
        Intent startCamCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(startCamCapture, CAMERA_START_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_START_CODE) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_START_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else toast(this, R.string.permission_cam_required);
        }
    }

    private void scanFile() {

    }

    private void init() {
        datePicker = new DatePickerFragment();
        updateFrag = new UpdateItemDialogFragment();
        final Button btnDatePicker = findViewById(R.id.btn_pick_date);
        btnDatePicker.setOnClickListener(view -> datePicker.show(getSupportFragmentManager(), null));
        final Button btnAddItemBill = findViewById(R.id.btn_add_item);
        btnAddItemBill.setOnClickListener(view -> {
            itemBillFrag = new AddItemBillFragment();
            itemBillFrag.show(getSupportFragmentManager(), null);
        });
        txtDate = findViewById(R.id.bill_date_view);
        setBill(ZonedDateTime.now());
        txtTotal = findViewById(R.id.txt_view_total);
        listView = findViewById(R.id.list_bill_id);
        listView.setOnItemClickListener((adapterView, view, i, l) -> toast(this, R.string.long_press_to_activate));
        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            ItemBill item = bill.getItem(position);
            selectedItem = item;
            updateFrag.show(getSupportFragmentManager(), null);
            return true;
        });
        btnAddBill = findViewById(R.id.add_bill_id);
        btnAddBill.setOnClickListener(view -> {
            if (bill.size() == 0) toast(this, R.string.add_bill_error);
            else if (bill.stream().map(ItemBill::getName).anyMatch(s -> s == null || !s.isBlank()))
                toast(this, R.string.add_bill_names_missing_error);
            else if (bill.stream().map(ItemBill::getPrice).anyMatch(Objects::isNull))
                toast(this, R.string.add_bill_price_missing_error);
            else {
                toast(this, R.string.bill_added);
            }
        });

        final Button btnCapture = findViewById(R.id.btn_scan_bill);
        btnCapture.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                takePicture();
            else requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_START_CODE);
        });

    }

    private void testList() {
        List<String> items = new ArrayList<>(25);
        for (int i = 1; i <= 25; i++) items.add("El" + i);
        ArrayAdapter<String> testElems = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(testElems);
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        init();
        //testList();
       // db connection
    }

    @Override
    public void onDateSet(final DatePicker datePicker, int year, int month, int day) {
        setBill(ZonedDateTime.of(LocalDate.of(year, month, day), LocalTime.now(), ZoneId.systemDefault()));
    }

    private void setBill(final ZonedDateTime pickedDate) {
        // Precondition check
        if (pickedDate == null) throw new NullPointerException("Cannot pass a null picked date!");

        // Object creation and setting check
//        if (bill == null) {
//            final var newBill = Bill.of(pickedDate, Currency.getInstance(Locale.getDefault()));
//            if (newBill.isPresent()) bill = newBill.get();
//            else throw new UnexpectedOptionalException("Base bill not created in optional for some reason.");
//        } else bill.setBillDate(pickedDate);
//        // -- //
//
//        txtDate.setText(formatter.format(bill.getBillDate()));
    }


    @Override
    public void addItemBill(final String name, final BigDecimal price) {
        // Precondition checking
//        if (name == null) throw new NullPointerException("Bill item names must not be null");
//       // else if (price != null && price.getQuantity().compareTo(BigDecimal.ZERO) < 0)
//            crash("Bill items must have a positive price");

        // Object build and insert check
//        var newBillItem = ItemBill.of(name, price, bill);
//        if (newBillItem.isPresent()) {
           // boolean insertion = bill.addItem(newBillItem.get());
           // if (!insertion) crash("Failed to insert the item bill in collection");
       // } else crash("Error when creating the item bill");

        // Collection and list update check
        if (bill.size() > 0) {
            updateListView();
            listView.setVisibility(View.VISIBLE);
        } else crash("Collection failed to update somehow!");
    }

    @Override
    public void modifyItemBill(String name, BigDecimal price) {
        // Precondition checking
        if (selectedItem == null) crash("Selected item should have been stored!");
        if (name == null) crash("Bill item names must not be null");
        else if (price != null && price.compareTo(BigDecimal.ZERO) < 0)
            crash("Bill items must have a positive price");

        selectedItem.setName(name);
        selectedItem.setPrice(price);
        bill.calcTotal();
        updateListView();
    }

    private void updateListView() {
        final AtomicInteger counter = new AtomicInteger(0);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                bill.stream()
                        .sorted()
                        .map(i -> {
                            String name = i.getName();
                            BigDecimal quantity = i.getPrice();
                            return "#" + counter.addAndGet(1) + ": "
                                    + (name != null && !name.isBlank() ? name : "Unnamed")
                                    + "  -  "
                                    + (quantity != null ? formatCurrency(quantity) : "No price set");
                        }).collect(Collectors.toList()
                        )));
        updateTotal();
    }

    //@SuppressLint("SetTextI18n")
    private void updateTotal() {
        var total = Optional.ofNullable(bill.getTotal());
        total.ifPresent(bigDecimal -> txtTotal.setText("Total: " + formatCurrency(bigDecimal)));
    }

    @Override
    public void apply(final UpdateType type) {
        if (type == null) crash("Cannot pass null update types");
        if (selectedItem == null) crash("Selected item should not be null");
        if (type == UpdateType.MODIFY) {
            Bundle bundle = new Bundle();
            var priceItem = selectedItem.getPrice();
            bundle.putString("IB_NAME", selectedItem.getName());
           // bundle.putString("IB_PRICE", priceItem == null ? "" : priceItem.getQuantity().toPlainString());
            itemBillFrag = new AddItemBillFragment();
            itemBillFrag.setArguments(bundle);
            itemBillFrag.show(getSupportFragmentManager(), null);
        } else if (type == UpdateType.DELETE){
           // bill.removeItem(selectedItem);
            updateListView();
            //if (bill.length() == 0) listView.setVisibility(View.GONE);
        }
    }
}