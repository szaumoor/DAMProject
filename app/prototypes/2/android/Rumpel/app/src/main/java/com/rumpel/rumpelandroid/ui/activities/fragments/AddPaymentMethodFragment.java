package com.rumpel.rumpelandroid.ui.activities.fragments;

import static com.rumpel.rumpelandroid.utils.Popups.toast;

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
//import com.rumpel.rumpelandroid.ui.activities.interfaces.InjectorPaymentMethod;

public class AddPaymentMethodFragment extends DialogFragment {
  //  private InjectorPaymentMethod injector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_pm_fragment, container, false);
        final Button insertBtn = rootView.findViewById(R.id.btn_pm_id);
        final EditText nameEditText = rootView.findViewById(R.id.edit_text_pm_name);
        insertBtn.setOnClickListener(view -> {
            String text = nameEditText.getText().toString();
            if (text.length() >= 3) {
              //  injector.addPaymentMethod(nameEditText.getText().toString());
                dismiss();
            } else toast(getContext(), R.string.pm_error);
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
      //  if (context instanceof InjectorPaymentMethod) injector = (InjectorPaymentMethod) context;
        //else crash("This fragment cannot be attached to anything other than Injectors");
    }
}