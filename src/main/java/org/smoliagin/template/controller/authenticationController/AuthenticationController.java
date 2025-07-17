package org.smoliagin.template.controller.authenticationController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smoliagin.template.controller.authenticationController.model.ModelRegistrationRequest;
import org.smoliagin.template.mapper.UserMapper;
import org.smoliagin.template.service.authenticationService.AuthenticationService;
import org.smoliagin.template.service.authenticationService.dto.AuthenticationResponseDto;
import org.smoliagin.template.service.authenticationService.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    @Operation(summary = "Регистрация пользователя",
            description = "Пароль должен содержать минимум 8 символов, буквы и цифры")
    public String register(
           @Valid @RequestBody ModelRegistrationRequest modelRegistration) {

        return authenticationService.register(userMapper.toDto(modelRegistration));
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        return authenticationService.refreshToken(request, response);
    }
}
