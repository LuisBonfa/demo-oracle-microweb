package com.demooracle.domain.error;

import com.ultraschemer.microweb.error.StandardException;

public class UserRoleSetException extends StandardException {
    public UserRoleSetException(String message) {
        super("ae0f9224-784a-4e07-bcac-faa388c8d057", 500, message);
    }
}
