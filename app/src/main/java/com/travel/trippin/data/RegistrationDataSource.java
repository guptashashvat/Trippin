package com.travel.trippin.data;

import com.travel.trippin.data.model.Tripper;
import com.travel.trippin.sql.DatabaseHelper;

import java.io.IOException;

public class RegistrationDataSource {

    public Result<Tripper> result(String firstName, String lastName, String email, String tripperName,
                                  String password, DatabaseHelper db) {
        try {
            if (db.checkTripper(email) || db.checkTripper(tripperName)) {
                return new Result.Error(new Exception("The email/tripperName you entered is already in. Kindly use a different email/tripperName."));
            }
            Tripper tripper = new Tripper(firstName, lastName, email, tripperName, password);
            db.addTripper(tripper);
            return new Result.Success<>(tripper);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error in Registration.", e));
        }
    }
}
