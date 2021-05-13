package com.travel.trippin.activities.ui.registration;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travel.trippin.R;
import com.travel.trippin.data.RegistrationRepository;
import com.travel.trippin.data.Result;
import com.travel.trippin.data.model.LoggedInTripper;
import com.travel.trippin.data.model.Tripper;
import com.travel.trippin.sql.DatabaseHelper;

import java.util.regex.Pattern;

public class RegistrationViewModel extends ViewModel {

    private MutableLiveData<RegistrationFormState> registrationFormState = new MutableLiveData<>();
    private MutableLiveData<RegistrationResult> registrationResult = new MutableLiveData<>();
    private RegistrationRepository registrationRepository;

    RegistrationViewModel(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public LiveData<RegistrationFormState> getRegistrationFormState() { return registrationFormState; }
    public LiveData<RegistrationResult> getRegistrationResult() { return registrationResult; }

    public void register(String firstName, String lastName, String email, String tripperName,
                         String password, DatabaseHelper db) {
        Result<Tripper> result = registrationRepository.register(firstName, lastName, email, tripperName,
                password, db);

        if (result instanceof Result.Success) {
            registrationResult.setValue(new RegistrationResult(((Result.Success<Tripper>) result).getData()));
        } else {
            Result.Error err = (Result.Error) result;
            registrationResult.setValue(new RegistrationResult(err.getError().getMessage()));
        }
    }

    public void registrationDataChanged(String firstName, String lastName, String email, String tripperName,
                                        String password, String confirmPassword) {
        if (isFirstNameValid(firstName) != 1) {
            registrationFormState.setValue(new RegistrationFormState(R.string.invalid_firstName, null,
                    null, null, null, null));
        } else if (isLastNameValid(lastName) != 1) {
            registrationFormState.setValue(new RegistrationFormState(null, R.string.invalid_lastName,
                    null, null, null, null));
        } else if (isEmailValid(email) != 1) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    R.string.invalid_email, null, null, null));
        } else if (isTripperNameValid(tripperName) != 1) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, R.string.invalid_tripperName, null, null));
        } else if (isPasswordValid(password) != 1) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, null, R.string.invalid_password, null));
        } else if (isConfirmPasswordValid(password, confirmPassword) != 1) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, null, null, R.string.confirm_password_err));
        } else {
            registrationFormState.setValue(new RegistrationFormState(true));
        }
    }

    private Integer isFirstNameValid(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return R.string.required_field_err;
        } else if (!Pattern.compile("^[a-zA-Z]{3,50}$").matcher(firstName).matches()) {
            return R.string.invalid_firstName;
        }
        return 1;
    }
    private Integer isLastNameValid(String lastName) {
        if (lastName != null && !lastName.trim().isEmpty()
                && !Pattern.compile("^[a-zA-Z]{3,50}$").matcher(lastName).matches()) {
            return R.string.invalid_lastName;
        }
        return 1;
    }
    private Integer isEmailValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return R.string.required_field_err;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return R.string.invalid_email;
        }
        return 1;
    }
    private Integer isTripperNameValid(String tripperName) {
        if (tripperName == null || tripperName.trim().isEmpty()) {
            return R.string.required_field_err;
        } else if (!Pattern.compile("^[a-zA-Z0-9_]{3,15}$").matcher(tripperName).matches()) {
            return R.string.invalid_tripperName;
        }
        return 1;
    }
    private Integer isPasswordValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            return R.string.required_field_err;
        } else if (password.trim().length() <= 5) {
            return R.string.invalid_password_length;
        }
        return 1;
    }
    private Integer isConfirmPasswordValid(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return R.string.required_field_err;
        } else if (!password.equals(confirmPassword)) {
            return R.string.confirm_password_err;
        }
        return 1;
    }
}