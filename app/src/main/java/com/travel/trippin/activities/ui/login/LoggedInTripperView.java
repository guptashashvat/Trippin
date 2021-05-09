package com.travel.trippin.activities.ui.login;

import com.travel.trippin.data.model.Tripper;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInTripperView {
    private String displayName;
    private Tripper tripper;
    //... other data fields that may be accessible to the UI

    LoggedInTripperView(Tripper tripper) {
        this.tripper = tripper;
        this.displayName = tripper.getTripperName();
    }

    public String getDisplayName() {
        return displayName;
    }
    public Tripper getTripper() { return tripper; }
}