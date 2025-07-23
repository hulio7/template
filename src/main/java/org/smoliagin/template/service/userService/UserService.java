package org.smoliagin.template.service.userService;

import jakarta.validation.constraints.NotNull;
import org.smoliagin.template.infrastructure.output.data.criteria.EntityList;
import org.smoliagin.template.infrastructure.output.data.criteria.GetFilteredAndSortedUserListCommand;
import org.smoliagin.template.service.userService.dto.UserDto;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    EntityList<UserDtoResponse> getAllUsers(GetFilteredAndSortedUserListCommand searchCommand);

    UserDtoResponse getUser(@NotNull Long id);

    UserDtoResponse updateUserById(UserDto dto, @NotNull Long id);

    String deleteUserById(@NotNull Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}


