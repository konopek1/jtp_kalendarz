package com.example.kalendarz.utils;

import android.widget.TextView;

/**
 * Helper CLass for validation
 */
public class Validator {
    private static final String ERROR_MESSAGE_EMPTY = "Can't be empty";

    private Validator() {
    }

    /**
     * Validate text view s with @TextViewValidatorLambda l Lambda
     * @param message the message
     * @param l       the l
     * @param args    the args
     * @return true if all args are valid
     */
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

