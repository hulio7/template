package org.smoliagin.template.controller.authenticationController.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ModelRegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 30)
    private String username;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Пароль должен содержать минимум 8 символов, буквы и цифры")
    private String password;
}
