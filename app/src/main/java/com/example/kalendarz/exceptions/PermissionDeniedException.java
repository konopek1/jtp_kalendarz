package com.example.kalendarz.exceptions;

import androidx.annotation.Nullable;

/**
 * The type Permission denied exception.
 */
public class PermissionDeniedException extends Exception {
    private String target;

    /**
     * Instantiates a new Permission denied exception.
     *
     * @param target the target
     */
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
