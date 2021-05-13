package com.travel.trippin.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class HelperUtility {

    /**
     * method to Hide keyboard
     */
    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * This method is to empty all input edit text
     */
    public static void emptyInputEditText(EditText...editTexts) {
        for (EditText editText : editTexts) {
            editText.setText(null);
        }
    }

    /**
     * This method is to set asterisk on required input edit text
     */
    public static void setAsteriskOnHint(EditText...editTexts) {
        for (EditText editText : editTexts) {
            editText.setHint(editText.getHint() + "*");
        }
    }

}
