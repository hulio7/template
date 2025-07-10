package org.smoliagin.template.service.userService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.smoliagin.template.mapper.UserMapper;
import org.smoliagin.template.repository.userRepository.UserRepository;
import org.smoliagin.template.repository.userRepository.entity.User;
import org.smoliagin.template.service.userService.dto.UserDto;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.smoliagin.template.util.exceptions.ExceptionFactory.entityNotFoundException;
import static org.smoliagin.template.util.messageSource.ErrorsMessage.UserMessage.USER_DELETE;
import static org.smoliagin.template.util.messageSource.ErrorsMessage.UserMessage.USER_NOT_EXIST;
import static org.smoliagin.template.util.messageSource.MessageSourceFactory.getMessage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDtoResponse createUser(UserDto dto) {
        User user = userMapper.toUser(dto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDtoResponse> getAllUsers() {
        List<User> listEntity = userRepository.findAll();
        return userMapper.toListDto(listEntity);
    }

    @Override
    public UserDtoResponse getUser(Long id) {
        User entity = userRepository.findById(id).orElseThrow(()-> entityNotFoundException(USER_NOT_EXIST, id));
        return userMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UserDtoResponse updateUserById(UserDto dto, @NotNull Long id) {
        User entity = userRepository.findById(id).orElseThrow(()->entityNotFoundException(USER_NOT_EXIST, id));
        User newEntity = userMapper.toUser(entity, dto);
        return userMapper.toDto(userRepository.save(newEntity));
    }

    @Override
    public String deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return getMessage(USER_DELETE, id);
        }
        throw entityNotFoundException(USER_NOT_EXIST, id);
    }
}
