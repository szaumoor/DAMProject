package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.NumUtils.formatCurrency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rumpel.rumpelandroid.R;
import com.szaumoor.rumple.model.entities.Bill;

import java.util.List;

public class BillGridAdapter extends ArrayAdapter<Bill> {

    public BillGridAdapter(@NonNull Context context, int resource, @NonNull List<Bill> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_layout, parent, false);
        }

        Bill bill = getItem(position);
        TextView txtView = convertView.findViewById(R.id.txt_view_card);
        String text = formatCurrency(bill.getTotal()) + "\n" + bill.size() + " items";
        txtView.setText(text);

        return convertView;
    }
}
