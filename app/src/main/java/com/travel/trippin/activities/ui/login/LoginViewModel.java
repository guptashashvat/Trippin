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
        if (isEmailOrTripperNameValid(emailOrTripperName) != 1) {
            loginFormState.setValue(new LoginFormState(isEmailOrTripperNameValid(emailOrTripperName), null));
        } else if (isPasswordValid(password) != 1) {
            loginFormState.setValue(new LoginFormState(null, isPasswordValid(password)));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private Integer isEmailOrTripperNameValid(String emailOrTripperName) {
        if (emailOrTripperName == null || emailOrTripperName.trim().isEmpty()) {
            return R.string.required_field_err;
        }
        return 1;
    }

    // A placeholder password validation check
    private Integer isPasswordValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            return R.string.required_field_err;
        } else if (password.trim().length() <= 5) {
            return R.string.invalid_password_length;
        }
        return 1;
    }
}