package com.rumpel.rumpelandroid.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rumpel.rumpelandroid.R;

public class AboutDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        final Button okButton = rootView.findViewById(R.id.btn_modify_id);
        okButton.setOnClickListener(view -> dismiss());

        final TextView aboutTitle = rootView.findViewById(R.id.about_title);
        final String title = getString(R.string.app_name) + "  |  " + getString(R.string.version);
        aboutTitle.setText(title);

        final TextView bodyTxtView = rootView.findViewById(R.id.about_heading_author);
        final String body = getString(R.string.developer) + ": " + getString(R.string.author_name)
                + "\n" + getString(R.string.author_contact) + ": " + getString(R.string.author_email);
        bodyTxtView.setText(body);

        final TextView poweredTxtView = rootView.findViewById(R.id.about_powered_by);
        final String pwString = getString(R.string.about_powered_by);
        poweredTxtView.setText(pwString);

        return rootView;
    }
}