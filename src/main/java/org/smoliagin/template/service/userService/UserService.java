package org.smoliagin.template.service.userService;

import jakarta.validation.constraints.NotNull;
import org.smoliagin.template.service.userService.dto.UserDto;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDtoResponse createUser(UserDto dto);

    List<UserDtoResponse> getAllUsers();

    UserDtoResponse getUser(@NotNull Long id);

    UserDtoResponse updateUserById(UserDto dto, @NotNull Long id);

    String deleteUserById(@NotNull Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


}

