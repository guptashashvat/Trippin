package com.travel.trippin.activities.ui.registration;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class RegistrationResult {
    @Nullable
    private RegisteredTripperView success;
    @Nullable
    private String error;

    RegistrationResult(@Nullable String error) {
        this.error = error;
    }

    RegistrationResult(@Nullable RegisteredTripperView success) {
        this.success = success;
    }

    @Nullable
    public RegisteredTripperView getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}