package org.smoliagin.template.controller.authenticationController.model;

import lombok.Data;

@Data
public class UserModelRegistrationRequest {

    private String username;
    private String email;
    private String password;
}
