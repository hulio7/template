package org.smoliagin.template.controller.userController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.smoliagin.template.controller.userController.model.UserModelCreateRequest;
import org.smoliagin.template.controller.userController.model.UserModelResponse;
import org.smoliagin.template.controller.userController.model.UserModelUpdateRequest;
import org.smoliagin.template.mapper.UserMapper;
import org.smoliagin.template.service.userService.UserService;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/user")
    @Operation(summary = "Создание пользователя"
            , description = "Пароль должен содержать минимум 8 символов, буквы и цифры")
    public UserModelResponse createUser (@RequestBody @Valid UserModelCreateRequest modelRequest) {
        UserDtoResponse dto= userService.createUser(userMapper.toDto(modelRequest));
        return userMapper.toModelResponse(dto);
    }

    @GetMapping("/users")
    @Operation(summary = "Получения списка пользователей")
    public List<UserModelResponse> getAllUsers () {
        List<UserDtoResponse> listDto = userService.getAllUsers();
        return userMapper.toListModelResponse(listDto);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Получения пользователя по ID")
    public UserModelResponse getUserById (@NotNull @PathVariable Long id) {
        UserDtoResponse dtoResponse = userService.getUser(id);
        return userMapper.toModelResponse(dtoResponse);
    }

    @PostMapping("/user/{id}")
    @Operation(summary = "Обновление пользователя по id")
    public UserModelResponse updateUserById (@NotNull @PathVariable Long id
            , @RequestBody @Valid UserModelUpdateRequest modelRequest) {
        UserDtoResponse dtoResponse = userService.updateUserById(userMapper.toDto(modelRequest), id);
        return userMapper.toModelResponse(dtoResponse);
    }

    @DeleteMapping("user/{id}")
    @Operation(summary = "Удаление пользователя по ID")
    public String deleteUserById (@PathVariable @NotNull Long id) {
       return userService.deleteUserById(id);

    }
}
