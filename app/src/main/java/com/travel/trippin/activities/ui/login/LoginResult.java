package com.travel.trippin.activities.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInTripperView success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInTripperView success) {
        this.success = success;
    }

    @Nullable
    public LoggedInTripperView getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}