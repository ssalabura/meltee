package com.ssalabura.meltee.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;

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

    public void login(String username, String password) {
        MelteeRealm.getApp().loginAsync(Credentials.emailPassword(username, password), result -> {
            if(result.isSuccess()) {
                loginResult.setValue(new AuthResult(new AuthUserDetails(result.get(), username)));
            } else {
                loginResult.setValue(new AuthResult(R.string.login_failed));
                System.out.println("Login failed: " + result.getError().getErrorMessage());
            }
        });
    }

    public void register(String username, String password, Activity activity) {
        MelteeRealm.getApp().getEmailPassword().registerUserAsync(username, password, result -> {
            if(result.isSuccess()) {
                registerResult.setValue(new AuthResult(new AuthUserDetails(null, username)));
                login(username, password);
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
                username.trim().length() > 3 &&
                username.trim().length() <= 20;
    }

    private boolean isPasswordValid(String password) {
        return password != null &&
                password.trim().length() > 5;
    }
}