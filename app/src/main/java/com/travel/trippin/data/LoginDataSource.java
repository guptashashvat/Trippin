package com.travel.trippin.data;

import com.travel.trippin.data.model.LoggedInTripper;
import com.travel.trippin.data.model.Tripper;
import com.travel.trippin.sql.DatabaseHelper;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInTripper> login(String emailOrTripperName, String password, DatabaseHelper db) {
        try {
            Tripper tripper = db.getTripper(emailOrTripperName, password);
            if (tripper == null) {
                if (db.checkTripper(emailOrTripperName))
                    return new Result.Error(new Exception("Wrong password"));
                return new Result.Error(new Exception("Oops! Tripper doesn't exist. Don't be shy, register yourself."));
            }
            LoggedInTripper loggedInTripper =
                    new LoggedInTripper(tripper);
            return new Result.Success<>(loggedInTripper);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}