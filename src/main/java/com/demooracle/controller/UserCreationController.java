package com.demooracle.controller;

import com.demooracle.controller.bean.CreateUserData;
import com.demooracle.controller.bean.NewUserData;
import com.demooracle.domain.DemooracleUserManagement;
import com.ultraschemer.microweb.domain.UserManagement;
import com.ultraschemer.microweb.domain.bean.UserData;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class UserCreationController extends SimpleController {
    public UserCreationController() {
        super(500, "4866e351-086d-4c03-b74c-7f1e4dcf3259");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws StandardException {
        NewUserData newUserData = Json.decodeValue(routingContext.getBodyAsString(), NewUserData.class);
        CreateUserData userData = newUserData.getUserData();
        String role = newUserData.getRoles().get(0);

        // Register the user, with the first given role:
        DemooracleUserManagement.registerSimpleUser(userData, role);

        // Assign the other given roles to the user:
        UserData newUser = UserManagement.loadUser(userData.getName());
        newUserData.getRoles().remove(role);

        for(String roleName: newUserData.getRoles()) {
            DemooracleUserManagement.setRoleToUser(newUser.getId(), roleName);
        }

        response.setStatusCode(200);
        response.end(Json.encode(UserManagement.loadUser(userData.getName())));
    }
}
