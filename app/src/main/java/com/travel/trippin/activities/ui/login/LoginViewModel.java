package com.travel.trippin.activities.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.travel.trippin.data.LoginRepository;
import com.travel.trippin.data.Result;
import com.travel.trippin.data.model.LoggedInTripper;
import com.travel.trippin.R;
import com.travel.trippin.sql.DatabaseHelper;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String emailOrTripperName, String password, DatabaseHelper db) {
        // can be launched in a separate asynchronous job
        Result<LoggedInTripper> result = loginRepository.login(emailOrTripperName, password, db);

        if (result instanceof Result.Success) {
            LoggedInTripper data = ((Result.Success<LoggedInTripper>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInTripperView(data.getTripper())));
        } else {
            loginResult.setValue(new LoginResult(((Result.Error) result).getError().getMessage()));
        }
    }

    public void loginDataChanged(String emailOrTripperName, String password) {
        if (!isEmailOrTripperNameValid(emailOrTripperName)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_trippername, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailOrTripperNameValid(String emailOrTripperName) {
        if (emailOrTripperName == null) {
            return false;
        }
        if (emailOrTripperName.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(emailOrTripperName).matches();
        } else {
            return !emailOrTripperName.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}