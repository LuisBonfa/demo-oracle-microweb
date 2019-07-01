package com.demooracle.controller.bean;

import net.sf.oval.constraint.EqualToField;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

public class CreateUserData {
    /**
     * User name.
     */
    @NotNull
    @NotEmpty
    @Length(min = 5, max = 128)
    private String name;

    /**
     * The user alias, or social name:
     */
    @NotNull
    @NotEmpty
    private String alias;

    /**
     * The password
     */
    @NotNull
    @NotEmpty
    @Length(min = 5, max = 64)
    private String password;

    /**
     * The password confirmation
     */
    @NotNull
    @NotEmpty
    @Length(min = 5, max = 64)
    @EqualToField("password")
    private String passwordConfirmation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
