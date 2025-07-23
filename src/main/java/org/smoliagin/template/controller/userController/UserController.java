package org.smoliagin.template.controller.userController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.smoliagin.template.controller.userController.model.UserModelResponse;
import org.smoliagin.template.controller.userController.model.UserModelUpdateRequest;
import org.smoliagin.template.infrastructure.output.data.criteria.EntityList;
import org.smoliagin.template.infrastructure.output.data.criteria.GetFilteredAndSortedUserListCommand;
import org.smoliagin.template.mapper.UserMapper;
import org.smoliagin.template.service.userService.UserService;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.smoliagin.template.infrastructure.output.data.criteria.WebUtils.getCriteria;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    @Operation(summary = "Получения списка пользователей", description = "в search используется : вместо =")
    public EntityList<UserModelResponse> getAllUsers (@RequestParam(required = false,  defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size,
                                                           @RequestParam(required = false, defaultValue = "id,desc") String sort,
                                                           @RequestParam(required = false, value = "search") String search) {
        var criteria = getCriteria(search);
        var searchCommand = GetFilteredAndSortedUserListCommand
                .builder()
                .page(page)
                .size(size)
                .sort(sort)
                .criteria(criteria)
                .build();
        var listDto = userService.getAllUsers(searchCommand);
        return new EntityList<>(
                listDto.getTotalItems(),
                listDto.getItems().stream().map(userMapper::toModelResponse).collect(Collectors.toList()));
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Получения пользователя по ID")
    public UserModelResponse getUserById (@NotNull @PathVariable Long id) {
        UserDtoResponse dtoResponse = userService.getUser(id);
        return userMapper.toModelResponse(dtoResponse);
    }

    @PostMapping("/user/{id}")
    @Operation(summary = "Обновление пользователя по ID")
    public UserModelResponse updateUserById (@NotNull @PathVariable Long id
            , @RequestBody @Valid UserModelUpdateRequest modelRequest) {
        UserDtoResponse dtoResponse = userService.updateUserById(userMapper.toDto(modelRequest), id);
        return userMapper.toModelResponse(dtoResponse);
    }

    @DeleteMapping("user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление пользователя по ID")
    public String deleteUserById (@PathVariable @NotNull Long id) {
       return userService.deleteUserById(id);

    }
}
