package com.travel.trippin.activities;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.travel.trippin.R;
import com.travel.trippin.activities.ui.login.LoggedInTripperView;
import com.travel.trippin.activities.ui.login.LoginViewModel;
import com.travel.trippin.activities.ui.login.LoginViewModelFactory;
import com.travel.trippin.databinding.ActivityLoginBinding;
import com.travel.trippin.sql.DatabaseHelper;
import com.travel.trippin.util.HelperUtility;

public class LoginActivity extends AppCompatActivity {

    private final AppCompatActivity activity = LoginActivity.this;
    public static final String EXTRA_TRIPPER_NAME = "tripperName";
    EditText tripperNameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    ProgressBar loadingProgressBar;

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        initObjects();
        initListeners();

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getTrippernameError() != null) {
                tripperNameEditText.setError(getString(loginFormState.getTrippernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
        });
        if (getIntent().getExtras() != null) {
            tripperNameEditText.setText(getIntent().getStringExtra(EXTRA_TRIPPER_NAME));
            passwordEditText.requestFocus();
        }
    }

    private void initViews() {
        tripperNameEditText = binding.trippername;
        passwordEditText = binding.password;
        loginButton = binding.login;
        registerButton = binding.register;
        loadingProgressBar = binding.loading;
    }

    private void initObjects() {
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        db = new DatabaseHelper(activity);
    }

    private void initListeners() {
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(tripperNameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        tripperNameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(tripperNameEditText.getText().toString(),
                        passwordEditText.getText().toString(), db);
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(tripperNameEditText.getText().toString(),
                    passwordEditText.getText().toString(), db);
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(activity, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void updateUiWithUser(LoggedInTripperView model) {
        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

        setResult(Activity.RESULT_OK);

        //Complete and destroy login activity once successful
        finish();
    }

    private void showLoginFailed(String errMsg) {
        HelperUtility.hideKeyboardFrom(activity);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), errMsg, 12000);
        snackbar.setAction("Clear Fields!", v -> {
            HelperUtility.emptyInputEditText(tripperNameEditText, passwordEditText);
            Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
        });
        snackbar.show();
    }
}