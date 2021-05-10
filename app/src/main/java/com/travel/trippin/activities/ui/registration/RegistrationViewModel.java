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
        if (!isFirstNameValid(firstName)) {
            registrationFormState.setValue(new RegistrationFormState(R.string.invalid_firstName, null,
                    null, null, null, null));
        } else if (!isLastNameValid(lastName)) {
            registrationFormState.setValue(new RegistrationFormState(null, R.string.invalid_lastName,
                    null, null, null, null));
        } else if (!isEmailValid(email)) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, R.string.invalid_email, null, null));
        } else if (!isTripperNameValid(tripperName)) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, R.string.invalid_tripperName, null, null));
        } else if (!isPasswordValid(password)) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, null, R.string.invalid_password, null));
        } else if (!isConfirmPasswordValid(password, confirmPassword)) {
            registrationFormState.setValue(new RegistrationFormState(null, null,
                    null, null, null, R.string.confirm_password_err));
        } else {
            registrationFormState.setValue(new RegistrationFormState(true));
        }
    }

    private boolean isFirstNameValid(String firstName) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            return "^[a-zA-Z]{3,50}$".matches(firstName);
        }
        return false;
    }
    private boolean isLastNameValid(String lastName) {
        if (lastName != null && !lastName.trim().isEmpty()) {
            return "^[a-zA-Z]{3,50}$".matches(lastName);
        }
        return true;
    }
    private boolean isEmailValid(String email) {
        if (email != null && !email.trim().isEmpty()) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }
    private boolean isTripperNameValid(String tripperName) {
        if (tripperName != null && !tripperName.trim().isEmpty()) {
            return "^[a-zA-Z0-9_]{3,15}$".matches(tripperName);
        }
        return false;
    }
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}