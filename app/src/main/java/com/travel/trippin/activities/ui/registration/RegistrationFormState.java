package com.travel.trippin.activities.ui.registration;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
public class RegistrationFormState {
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer tripperNameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    private boolean isDataValid;

    public RegistrationFormState(@Nullable Integer firstNameError, @Nullable Integer lastNameError,
                                 @Nullable Integer emailError, @Nullable Integer tripperNameError,
                                 @Nullable Integer passwordError, @Nullable Integer confirmPasswordError) {
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.emailError = emailError;
        this.tripperNameError = tripperNameError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.isDataValid = false;
    }

    RegistrationFormState(boolean isDataValid) {
        this.firstNameError = null;
        this.lastNameError = null;
        this.emailError = null;
        this.tripperNameError = null;
        this.passwordError = null;
        this.confirmPasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getFirstNameError() {
        return firstNameError;
    }

    @Nullable
    public Integer getLastNameError() {
        return lastNameError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }

    @Nullable
    public Integer getTripperNameError() { return tripperNameError; }

    public boolean isDataValid() {
        return isDataValid;
    }
}