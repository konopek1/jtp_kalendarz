package com.example.kalendarz.exceptions;

import androidx.annotation.Nullable;

public class    PermissionDeniedException extends Exception {
    private String target;

    public PermissionDeniedException(String target) {
        super();
        this.target = target;
    }
    @Nullable
    @Override
    public String getMessage() {
        return "Permission on " + target +" denied";
    }
}
