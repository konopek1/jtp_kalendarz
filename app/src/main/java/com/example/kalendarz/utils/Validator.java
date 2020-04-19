package com.example.kalendarz.utils;

import android.widget.TextView;

public class Validator {
    private static final String ERROR_MESSAGE_EMPTY = "Can't be empty";

    private Validator() {
    }

    public static boolean validateTextView(String message, TextViewValidatorLambda l, TextView ...args) {
        boolean isValid = true;
        for(TextView textView : args) {
            String txt =  textView.getText().toString();
            if(!l.call(textView)) {
                textView.setError(message);
                isValid = false;
            }
        }
        return isValid;
    }
}

