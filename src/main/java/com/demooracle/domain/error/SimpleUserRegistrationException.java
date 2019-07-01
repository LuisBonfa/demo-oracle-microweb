package com.demooracle.domain.error;

import com.ultraschemer.microweb.error.StandardException;

public class SimpleUserRegistrationException extends StandardException {
    public SimpleUserRegistrationException(String message) {
        super("bde3aab4-0524-4899-be73-65a5b6d31268", 500, message);
    }
}
