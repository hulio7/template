package org.smoliagin.template.controller.userController.model;

import lombok.Data;

@Data
public class UserModelUpdateRequest {

    private String name;

    private String surname;

    private String email;

}
