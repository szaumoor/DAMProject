package com.rumpel.rumpelandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.realm.DAOUser;
import com.rumpel.rumpelandroid.realm.Outcome;
import com.rumpel.rumpelandroid.utils.AndroidUtils;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.UserPass;
import com.szaumoor.rumple.model.utils.types.Username;

import java.util.Optional;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DAOUser dao = new DAOUser(this);
        TextView loginBtn = findViewById(R.id.switchToLoginBtn);
        SpannableString spString = new SpannableString(loginBtn.getText());
        spString.setSpan(new UnderlineSpan(), 0, spString.length(), 0);
        loginBtn.setText(spString);
        loginBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

        EditText userTxt = findViewById(R.id.username);
        EditText emailTxt = findViewById(R.id.email);
        EditText passTxt = findViewById(R.id.password);
        EditText passConfirmTxt = findViewById(R.id.passwordConfirm);

        userTxt.setText("username_wizard_2");
        emailTxt.setText("username@gmail.com");
        passTxt.setText("password");
        passConfirmTxt.setText("password");

        Button signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(v -> {
            Context context = SignUpActivity.this;
            String username = userTxt.getText().toString();
            String email = emailTxt.getText().toString();
            String pass = passTxt.getText().toString();
            String passConfirm = passConfirmTxt.getText().toString();
            if (!pass.equals(passConfirm)) {
                AndroidUtils.toast(context, getString(R.string.passwords_dont_match));
                return;
            }
            Optional<User> user = User.of(new Username(username),
                    new UserEmail(email),
                    UserPass.of(pass.toCharArray()).get());
            if (user.isPresent()) {
                Outcome insert = dao.insert(user.get());
                if (insert == Outcome.SUCCESS)
                    AndroidUtils.toast(context, "User registered successfully");
                else if (insert == Outcome.UNIQUE_EXISTS)
                    AndroidUtils.toast(context, "User exists already");
                else if (insert == Outcome.TIMEOUT)
                    AndroidUtils.toast(context, "Connection timed out!");
                else Log.v("SIGN UP", "Failed to insert user!");
            } else AndroidUtils.toast(context, "Could not create the user, invalid parameters");
        });

    }
}