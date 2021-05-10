package com.travel.trippin.activities.ui.registration;

import androidx.annotation.Nullable;

import com.travel.trippin.data.model.Tripper;

/**
 * Authentication result : success (user details) or error message.
 */
public class RegistrationResult {
    @Nullable
    private Tripper success;
    @Nullable
    private String error;

    RegistrationResult(@Nullable String error) {
        this.error = error;
    }

    RegistrationResult(@Nullable Tripper success) {
        this.success = success;
    }

    @Nullable
    public Tripper getSuccess() { return success; }

    @Nullable
    public String getError() { return error; }
}