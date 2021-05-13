package com.travel.trippin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.travel.trippin.R;
import com.travel.trippin.activities.ui.registration.RegistrationViewModel;
import com.travel.trippin.activities.ui.registration.RegistrationViewModelFactory;
import com.travel.trippin.data.model.Tripper;
import com.travel.trippin.databinding.ActivityRegistrationBinding;
import com.travel.trippin.sql.DatabaseHelper;
import com.travel.trippin.util.HelperUtility;

public class RegistrationActivity extends AppCompatActivity {
    private final AppCompatActivity activity = RegistrationActivity.this;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText tripperNameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView loginQuestionText;

    private DatabaseHelper db;
    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        initObjects();
        initListeners();

        registrationViewModel.getRegistrationFormState().observe(this, registrationFormState -> {
            if(registrationFormState == null) {
                return;
            }
            registerButton.setEnabled(registrationFormState.isDataValid());
            if (registrationFormState.getFirstNameError() != null) {
                firstNameEditText.setError(getString(registrationFormState.getFirstNameError()));
            }
            if (registrationFormState.getLastNameError() != null) {
                lastNameEditText.setError(getString(registrationFormState.getLastNameError()));
            }
            if (registrationFormState.getEmailError() != null) {
                emailEditText.setError(getString(registrationFormState.getEmailError()));
            }
            if (registrationFormState.getTripperNameError() != null) {
                tripperNameEditText.setError(getString(registrationFormState.getTripperNameError()));
            }
            if (registrationFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(registrationFormState.getPasswordError()));
            }
            if (registrationFormState.getConfirmPasswordError() != null) {
                confirmPasswordEditText.setError(getString(registrationFormState.getConfirmPasswordError()));
            }
        });

        registrationViewModel.getRegistrationResult().observe(this, registrationResult -> {
            if (registrationResult == null) {
                return;
            }
            if (registrationResult.getError() != null) {
                registrationFailedAction(registrationResult.getError());
            }
            if (registrationResult.getSuccess() != null) {
                registrationSuccessAction(registrationResult.getSuccess());
            }
        });
    }

    private void initViews() {
        firstNameEditText = binding.firstName;
        lastNameEditText = binding.lastName;
        emailEditText = binding.email;
        tripperNameEditText = binding.tripperName;
        passwordEditText = binding.password;
        confirmPasswordEditText = binding.confirmPassword;
        registerButton = binding.register;
        loginQuestionText = binding.loginQuestion;

        HelperUtility.setAsteriskOnHint(firstNameEditText, emailEditText, tripperNameEditText,
                passwordEditText, confirmPasswordEditText);
    }

    private void initObjects() {
        registrationViewModel = new ViewModelProvider(this, new RegistrationViewModelFactory())
                .get(RegistrationViewModel.class);
        db = new DatabaseHelper(activity);
    }

    private void initListeners() {
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registrationViewModel.registrationDataChanged(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(), emailEditText.getText().toString(),
                        tripperNameEditText.getText().toString(), passwordEditText.getText().toString(),
                        confirmPasswordEditText.getText().toString());

            }
        };
        firstNameEditText.addTextChangedListener(afterTextChangedListener);
        lastNameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registrationViewModel.register(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(), emailEditText.getText().toString(),
                        tripperNameEditText.getText().toString(), passwordEditText.getText().toString(), db);
            }
            return false;
        });

        registerButton.setOnClickListener(v -> registrationViewModel.register(firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(), emailEditText.getText().toString(),
                tripperNameEditText.getText().toString(), passwordEditText.getText().toString(), db));

        SpannableString spannableString = new SpannableString(getString(R.string.login_question));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                finishAndMoveToAnotherActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 20, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginQuestionText.setText(spannableString);
        loginQuestionText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void registrationSuccessAction(Tripper tripper) {
        Toast.makeText(getApplicationContext(), R.string.registration_success, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_TRIPPER_NAME, tripper.getTripperName());
        finishAndMoveToAnotherActivity(intent);
    }

    private void registrationFailedAction(String error) {
        HelperUtility.hideKeyboardFrom(activity);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), error, 12000);
        snackbar.setAction(R.string.action_clear_fields, v -> {
            HelperUtility.emptyInputEditText(emailEditText, tripperNameEditText, passwordEditText,
                    confirmPasswordEditText);
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
        });
        snackbar.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            finishAndMoveToAnotherActivity(intent);
            return true;
        }
        return false;
    }

    private void finishAndMoveToAnotherActivity(Intent intent) {
        HelperUtility.hideKeyboardFrom(activity);
        startActivity(intent);
        HelperUtility.emptyInputEditText(firstNameEditText, lastNameEditText, emailEditText,
                tripperNameEditText, passwordEditText, confirmPasswordEditText);
        setResult(Activity.RESULT_OK);
        finish();
    }
}