package com.rumpel.rumpelandroid.activities.fragments;

import static com.rumpel.rumpelandroid.utils.AndroidUtils.crash;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.UpdateType;
import com.rumpel.rumpelandroid.activities.interfaces.Updater;

public class UpdateItemDialogFragment extends DialogFragment {

    private Updater updater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.update_item_fragment, container, false);
        final Button modifyButton = rootView.findViewById(R.id.btn_modify_id);
        final Button deleteButton = rootView.findViewById(R.id.btn_delete_id);
        modifyButton.setOnClickListener(view -> {
            if (updater == null) crash("Injector should never be null");
            updater.apply(UpdateType.MODIFY);
            dismiss();
        });
        deleteButton.setOnClickListener(view -> {
            if (updater == null) crash("Injector should never be null");
            updater.apply(UpdateType.DELETE);
            dismiss();
        });
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Updater) updater = (Updater) context;
        else crash("This fragment cannot be attached to anything other than Feedbackers");
    }
}