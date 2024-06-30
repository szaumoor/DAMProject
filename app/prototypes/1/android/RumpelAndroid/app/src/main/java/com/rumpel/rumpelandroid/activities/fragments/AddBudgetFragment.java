package com.rumpel.rumpelandroid.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rumpel.rumpelandroid.R;

public class AddBudgetFragment extends DialogFragment  {
    private TextView startDateTxt;
    private TextView endDateTxt;
    private Button startDateBtn;
    private Button endDateBtn;
    private EditText softLimitTxt;
    private EditText hardLimitTxt;
    private Button insertButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_budget_fragment, container, false);

        startDateTxt = rootView.findViewById(R.id.txt_start_date);
        endDateTxt = rootView.findViewById(R.id.txt_end_date);
        startDateBtn = rootView.findViewById(R.id.btn_pick_start_date);
        endDateBtn = rootView.findViewById(R.id.btn_pick_end_date);
        softLimitTxt = rootView.findViewById(R.id.edit_text_soft_limit);
        hardLimitTxt = rootView.findViewById(R.id.edit_text_hard_limit);
        insertButton = rootView.findViewById(R.id.btn_pm_id);
        
        startDateBtn.setOnClickListener(v -> new DatePickerFragment().show(getParentFragmentManager(), null));
        endDateBtn.setOnClickListener(v -> new DatePickerFragment().show(getParentFragmentManager(), null));


        return rootView;
    }


}