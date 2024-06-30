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
import com.rumpel.rumpelandroid.activities.interfaces.InjectorPaymentMethod;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorTag;

public class AddTagFragment extends DialogFragment {
    private InjectorTag injector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_tag_fragment, container, false);
        final Button insertBtn = rootView.findViewById(R.id.btn_tag_id);
        final EditText nameEditText = rootView.findViewById(R.id.edit_text_tag_name);
        insertBtn.setOnClickListener(view -> {
            String text = nameEditText.getText().toString();
            if (text.length() >= 3) {
                injector.addTag(nameEditText.getText().toString());
                dismiss();
            } else toast(getContext(), R.string.pm_error);
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof InjectorTag) injector = (InjectorTag) context;
        else crash("This fragment cannot be attached to anything other than Injectors");
    }
}