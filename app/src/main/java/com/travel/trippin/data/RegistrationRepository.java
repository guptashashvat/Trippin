package com.travel.trippin.data;

import com.travel.trippin.data.model.Tripper;
import com.travel.trippin.sql.DatabaseHelper;

public class RegistrationRepository {
    private static volatile RegistrationRepository instance;

    private RegistrationDataSource dataSource;

    private RegistrationRepository(RegistrationDataSource dataSource) { this.dataSource = dataSource; }

    public static RegistrationRepository getInstance(RegistrationDataSource dataSource) {
        if (instance == null) {
            instance = new RegistrationRepository(dataSource);
        }
        return instance;
    }

    public Result<Tripper> register(String firstName, String lastName, String email, String tripperName,
                                    String tripperPassword, DatabaseHelper db) {
        return dataSource.result(firstName, lastName, email, tripperName, tripperPassword, db);
    }
}
