package com.example.kalendarz.utils;

import android.widget.TextView;

/**
 * Interfejs dla wyrażenia lambda odpowiadającego za validacje danych
 */
public interface TextViewValidatorLambda {

    /**
     * @param contentInput Validowany element
     * @return Zwraca prawdę jesli wynik validacji jest pozytywny
     */
    boolean call(TextView contentInput);
}
