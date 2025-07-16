package org.smoliagin.template.service.authenticationService.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {

    private String username;
    private String email;
    private String password;
}
