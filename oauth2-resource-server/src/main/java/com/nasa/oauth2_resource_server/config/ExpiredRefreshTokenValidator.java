package com.nasa.oauth2_resource_server.config;

import com.nasa.oauth2_resource_server.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
public class ExpiredRefreshTokenValidator implements OAuth2TokenValidator<Jwt> {
    @Autowired
    private TokenRepository tokenRepository;

    OAuth2Error error = new OAuth2Error(
            HttpStatus.UNAUTHORIZED.toString(),
            "The refresh token has expired",
            null
    );


    @Transactional
    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        Instant now = Instant.now();
        Instant expiresAt = token.getExpiresAt();
        UUID tokenId = UUID.fromString(token.getId());

        boolean isExpired = Duration.between(now, expiresAt).isNegative();

        if(isExpired) {
            tokenRepository.deleteById(tokenId);

            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();
    }
}
