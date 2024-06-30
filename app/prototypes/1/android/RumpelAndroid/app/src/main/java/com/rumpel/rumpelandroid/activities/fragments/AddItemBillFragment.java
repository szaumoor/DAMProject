package com.rumpel.rumpelandroid.activities.fragments;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;
import static com.rumpel.rumpelandroid.utils.AndroidUtils.toast;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorBill;

import java.math.BigDecimal;

public class AddItemBillFragment extends DialogFragment {
    private InjectorBill injector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_bill_fragment, container, false);
        final Button insertBtn = rootView.findViewById(R.id.btn_pm_id);
        final EditText nameEditText = rootView.findViewById(R.id.edit_text_pm_name);
        final EditText priceEditText = rootView.findViewById(R.id.edit_text_item_price);

        Bundle args = getArguments();
        String argsName = null;
        BigDecimal argsPrice = null;
        if (args != null) {
            argsName = args.getString("IB_NAME");
            argsPrice = new BigDecimal(args.getString("IB_PRICE"));
            insertBtn.setText(R.string.modify_item);
        }
        if (argsName != null) nameEditText.setText(argsName);

        priceEditText.setText(argsPrice != null ? argsPrice.toPlainString() : getString(R.string.default_item_price));
        insertBtn.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String price = fixFormat(priceEditText.getText().toString());
           // if (name.isBlank() || price.isBlank()) toast(getContext(), R.string.item_bill_check);
            //else
            if (!price.isBlank()) {
                String decimals = price.split("\\.")[1];
                if (decimals.length() > 2) {
                    toast(getContext(), R.string.item_bill_price_decimal_check);
                    final String fixedPrice = price.substring(0, price.indexOf(".") + 1) + decimals.substring(0,2);
                    priceEditText.setText(fixedPrice);
                }
            }
            if (injector != null) {
                BigDecimal resultingBigDecimal = price.isBlank() ? null : new BigDecimal(price);
//                if (args == null) injector.addItemBill(name, MoneyQuantity.of(resultingBigDecimal, Currency.getInstance(Locale.getDefault())).get());
//                else {
//                    injector.modifyItemBill(name, MoneyQuantity.of(resultingBigDecimal, Currency.getInstance(Locale.getDefault())).get());
//                    dismiss();
//                }
            } else crash("The injector shouldn't have been null at all");
        });
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof InjectorBill) injector = (InjectorBill) context;
        else crash("This fragment cannot be attached to anything other than Injectors");
    }

    private String fixFormat(String price){
        if (price.isBlank()) return price;
        if (price.endsWith("."))
            return price + "00";
        else if (!price.contains("."))
            return price + ".00";
        return price;
    }
}