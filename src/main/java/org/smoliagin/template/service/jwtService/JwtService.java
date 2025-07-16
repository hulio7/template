package org.smoliagin.template.service.jwtService;

import io.jsonwebtoken.Claims;
import org.smoliagin.template.repository.userRepository.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String extractUsername(String token);

    boolean isValid(String token, UserDetails user);

    boolean isValidRefresh(String token, User user);
}
