package com.ssalabura.meltee.ui.login;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ssalabura.meltee.MainActivity;
import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;

import io.realm.Realm;
import io.realm.mongodb.User;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        User user = MelteeRealm.getApp().currentUser();
        if(user != null && preferences.contains("username")) {
            goToMainActivity(new AuthUserDetails(user, preferences.getString("username","")));
        }

        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            boolean isDataValid = loginFormState.isDataValid();
            loginButton.setEnabled(isDataValid);
            registerButton.setEnabled(isDataValid);
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showErrorToast(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                saveCredentials(preferences, usernameEditText.getText().toString());
                showLoginToast(usernameEditText.getText().toString());
                goToMainActivity(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);
        });

        loginViewModel.getRegisterResult().observe(this, registerResult -> {
            if(registerResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if(registerResult.getError() != null) {
                showErrorToast(registerResult.getError());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        registerButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.register(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(), this);
        });

        if(preferences.contains("username")) {
            usernameEditText.setText(preferences.getString("username",""));
        }
    }

    private void saveCredentials(SharedPreferences preferences, String username) {
        preferences.edit().putString("username", username).apply();
    }

    private void showLoginToast(String username) {
        String welcome = getString(R.string.welcome) + " " + username;
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void goToMainActivity(AuthUserDetails model) {
        MelteeRealm.setConfig(model.getUser(), model.getDisplayName());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", model.getDisplayName());
        startActivity(intent);
    }

    private void showErrorToast(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}