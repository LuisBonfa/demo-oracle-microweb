package com.demooracle.controller.bean;

import java.util.List;

public class NewUserData {
    private CreateUserData userData;
    private List<String> roles;

    public CreateUserData getUserData() {
        return userData;
    }

    public void setUserData(CreateUserData userData) {
        this.userData = userData;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
