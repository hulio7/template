package org.smoliagin.template.service.authenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.smoliagin.template.service.authenticationService.dto.AuthenticationResponseDto;
import org.smoliagin.template.service.authenticationService.dto.LoginRequestDto;
import org.smoliagin.template.service.authenticationService.dto.RegistrationRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    String register(RegistrationRequestDto request);

    AuthenticationResponseDto authenticate(LoginRequestDto request);

    ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response);
}
