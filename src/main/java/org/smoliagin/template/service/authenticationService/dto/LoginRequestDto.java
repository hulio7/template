package org.smoliagin.template.service.authenticationService.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String username;
    private String password;

}
