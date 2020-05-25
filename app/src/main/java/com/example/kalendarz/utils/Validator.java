package com.example.kalendarz.utils;

import android.widget.TextView;

/**
 * Klasa Validująca
 */
public class Validator {
    private static final String ERROR_MESSAGE_EMPTY = "Can't be empty";

    private Validator() {
    }

    /**
     * @param message
     * @param l
     * @param args
     * @return
     * Przyjmuję lambde, która jest wywoływana na przyjętych argumntach ..args
     * jeśli zwróci fałsz , przypisywana jest jej errorMessage(setError)
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

