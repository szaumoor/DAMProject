package com.rumpel.rumpelandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.realm.DAOUser;
import com.rumpel.rumpelandroid.utils.AndroidUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signUpLogin = findViewById(R.id.switchToSignUpBtn);
        ProgressBar loading = findViewById(R.id.loading);
        SpannableString spString = new SpannableString(signUpLogin.getText());
        spString.setSpan(new UnderlineSpan(), 0, spString.length(), 0);
        signUpLogin.setText(spString);
        signUpLogin.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        DAOUser dao = new DAOUser(this);
        EditText usernameTxt = findViewById(R.id.username);
        EditText passTxt = findViewById(R.id.password);

        usernameTxt.setText("username");

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            boolean authenticate = dao.authenticate(usernameTxt.getText().toString(), passTxt.getText().toString());
            if (authenticate) {
                Log.v("MONGO", "Authenticated!");
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else AndroidUtils.toast(LoginActivity.this, getString(R.string.failed_to_authenticate));
            loading.setVisibility(View.INVISIBLE);
        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        final int inputType = passTxt.getInputType();
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) passTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            else passTxt.setInputType(inputType);
        });


    }

}