package org.smoliagin.template.service.userService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.smoliagin.template.service.userService.dto.UserDto;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse createUser(UserDto dto);

    List<UserDtoResponse> getAllUsers();

    UserDtoResponse getUser(@NotNull Long id);

    UserDtoResponse updateUserById(UserDto dto, @NotNull Long id);

    String deleteUserById(@NotNull Long id);

}

