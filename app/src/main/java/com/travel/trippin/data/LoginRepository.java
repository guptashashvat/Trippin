package com.travel.trippin.data;

import com.travel.trippin.data.model.LoggedInTripper;
import com.travel.trippin.sql.DatabaseHelper;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInTripper user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInTripper user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInTripper> login(String emailOrTripperName, String password, DatabaseHelper db) {
        // handle login
        Result<LoggedInTripper> result = dataSource.login(emailOrTripperName, password, db);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInTripper>) result).getData());
        }
        return result;
    }
}