package org.smoliagin.template.mapper;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.smoliagin.template.controller.userController.model.UserModelCreateRequest;
import org.smoliagin.template.controller.userController.model.UserModelResponse;
import org.smoliagin.template.controller.userController.model.UserModelUpdateRequest;
import org.smoliagin.template.repository.userRepository.entity.User;
import org.smoliagin.template.service.userService.dto.UserDto;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(UserModelCreateRequest modelRequest);

    UserModelResponse toModelResponse(UserDtoResponse dto);

    User toUser(UserDto dto);

    UserDtoResponse toDto(User entity);

    List<UserDtoResponse> toListDto(List<User> listEntity);

    List<UserModelResponse> toListModelResponse(List<UserDtoResponse> listAllUsers);

    UserDto toDto(@Valid UserModelUpdateRequest modelRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(@MappingTarget User entity, UserDto dto);
}

