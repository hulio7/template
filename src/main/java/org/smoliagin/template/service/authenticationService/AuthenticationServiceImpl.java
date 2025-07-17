package org.smoliagin.template.service.authenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.smoliagin.template.mapper.UserMapper;
import org.smoliagin.template.repository.tokenRepository.TokenRepository;
import org.smoliagin.template.repository.tokenRepository.entity.Token;
import org.smoliagin.template.repository.userRepository.UserRepository;
import org.smoliagin.template.repository.userRepository.entity.Role;
import org.smoliagin.template.repository.userRepository.entity.User;
import org.smoliagin.template.service.authenticationService.dto.AuthenticationResponseDto;
import org.smoliagin.template.service.authenticationService.dto.LoginRequestDto;
import org.smoliagin.template.service.authenticationService.dto.RegistrationRequestDto;
import org.smoliagin.template.service.jwtService.JwtServiceImpl;
import org.smoliagin.template.service.userService.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

import static org.smoliagin.template.util.exceptions.ExceptionFactory.entityNotFoundException;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_EMAIL_ALREADY_EXISTS;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_NAME_ALREADY_EXISTS;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_NOT_EXIST_NAME;
import static org.smoliagin.template.util.messageSource.Message.UserMessage.USER_WELCOME;
import static org.smoliagin.template.util.messageSource.MessageSourceFactory.getMessage;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final JwtServiceImpl jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    private final UserService userService;

    private final UserMapper userMapper;

    private static final Logger log = Logger.getLogger(AuthenticationServiceImpl.class.getName());

    @Transactional
    @Override
    public String register(RegistrationRequestDto dto) {

        if(userService.existsByUsername(dto.getUsername())) {
            return getMessage(USER_NAME_ALREADY_EXISTS, dto.getUsername());
        }

        if(userService.existsByEmail(dto.getEmail())) {
            return getMessage(USER_EMAIL_ALREADY_EXISTS, dto.getEmail());
        }

        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("User registration");
        return getMessage(USER_WELCOME, user.getUsername().toUpperCase());
    }

    @Transactional
    @Override
    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> entityNotFoundException(USER_NOT_EXIST_NAME, request.getUsername()));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> entityNotFoundException(USER_NOT_EXIST_NAME, username));

        if (jwtService.isValidRefresh(token, user)) {

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllToken(user);

            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponseDto(accessToken, refreshToken), HttpStatus.OK);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private void revokeAllToken(User user) {

        List<Token> validTokens = tokenRepository.findAllAccessTokenByUser(user.getId());

        if(!validTokens.isEmpty()){
            validTokens.forEach(t ->{
                t.setLoggedOut(true);
            });
        }

        tokenRepository.saveAll(validTokens);

    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
