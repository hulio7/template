package org.smoliagin.template.service.authenticationService.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDto {

    private final String accessToken;

    private final String refreshToken;
}
