package com.example.kalendarz.utils;

import android.widget.TextView;

/**
 * Lambda interface for Input Validation on TextViews
 */
public interface TextViewValidatorLambda {

    /**
     *
     *
     * @param contentInput element
     * @return Should return true if element is valid
     */
    boolean call(TextView contentInput);
}
