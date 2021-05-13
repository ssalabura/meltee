package com.ssalabura.meltee.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;

import io.realm.mongodb.Credentials;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AuthResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<AuthResult> registerResult = new MutableLiveData<>();

    LoginViewModel() {
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<AuthResult> getLoginResult() {
        return loginResult;
    }

    LiveData<AuthResult> getRegisterResult() {
        return registerResult;
    }

    public void login(String username, String password, Activity activity) {
        MelteeRealm.getApp().loginAsync(Credentials.emailPassword(username, password), result -> {
            if(result.isSuccess()) {
                MelteeRealm.setConfig(result.get(), username);
                saveCredentials(username, password, activity);
                loginResult.setValue(new AuthResult(new AuthUserDetails(username)));
            } else {
                loginResult.setValue(new AuthResult(R.string.login_failed));
            }
        });
    }

    public void register(String username, String password, Activity activity) {
        MelteeRealm.getApp().getEmailPassword().registerUserAsync(username, password, result -> {
            if(result.isSuccess()) {
                registerResult.setValue(new AuthResult(new AuthUserDetails(username)));
                login(username, password, activity);
            } else {
                registerResult.setValue(new AuthResult(R.string.register_failed));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        return username != null &&
                !username.contains("|") &&
                !username.trim().isEmpty() &&
                username.trim().length() <= 20;
    }

    private boolean isPasswordValid(String password) {
        return password != null &&
                password.trim().length() > 5;
    }

    private void saveCredentials(String username, String password, Activity activity) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}