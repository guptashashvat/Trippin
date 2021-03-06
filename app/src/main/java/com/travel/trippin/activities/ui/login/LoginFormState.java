package com.travel.trippin.activities.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
public class LoginFormState {
    @Nullable
    private Integer trippernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer trippernameError, @Nullable Integer passwordError) {
        this.trippernameError = trippernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.trippernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTrippernameError() {
        return trippernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}