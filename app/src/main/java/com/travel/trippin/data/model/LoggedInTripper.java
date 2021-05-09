package com.travel.trippin.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInTripper {

    private Tripper tripper;

    public LoggedInTripper(Tripper tripper) {
        this.tripper = tripper;
    }
    public Tripper getTripper() {
        return tripper;
    }
}