package org.smoliagin.template.service.userService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.smoliagin.template.infrastructure.output.data.criteria.EntityList;
import org.smoliagin.template.infrastructure.output.data.criteria.FilteringAndSortingAdapter;
import org.smoliagin.template.infrastructure.output.data.criteria.GetFilteredAndSortedUserListCommand;
import org.smoliagin.template.mapper.UserMapper;
import org.smoliagin.template.repository.userRepository.UserRepository;
import org.smoliagin.template.repository.userRepository.entity.User;
import org.smoliagin.template.service.userService.dto.UserDto;
import org.smoliagin.template.service.userService.dto.UserDtoResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.smoliagin.template.infrastructure.output.data.criteria.GetListFilterUtil.getListFilter;
import static org.smoliagin.template.util.exceptions.ExceptionFactory.entityNotFoundException;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_DELETE;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_NOT_EXIST;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_NOT_EXIST_NAME;
import static org.smoliagin.template.util.messageSource.MessageSourceFactory.getMessage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends FilteringAndSortingAdapter<User> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public EntityList<UserDtoResponse> getAllUsers(GetFilteredAndSortedUserListCommand searchCommand) {
        var filter = getListFilter(searchCommand, User.class);
        var listEntity = userRepository.findAll(getSpecification(filter), getPageable(filter));
        return new EntityList<>(listEntity.getTotalElements(), listEntity.map(userMapper::toDto).getContent());
    }

    @Override
    public UserDtoResponse getUser(Long id) {
        User entity = userRepository.findById(id).orElseThrow(()-> entityNotFoundException(USER_NOT_EXIST, id));
        return userMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UserDtoResponse updateUserById(UserDto dto, @NotNull Long id) {
        User user = userRepository.findById(id).orElseThrow(()->entityNotFoundException(USER_NOT_EXIST, id));
        User newEntity = userMapper.toUser(user, dto);
        return userMapper.toDto(userRepository.save(newEntity));
    }

    @Transactional
    @Override
    public String deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return getMessage(USER_DELETE, id);
        }
        throw entityNotFoundException(USER_NOT_EXIST, id);
    }

    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> entityNotFoundException(USER_NOT_EXIST_NAME, username));
    }
}
