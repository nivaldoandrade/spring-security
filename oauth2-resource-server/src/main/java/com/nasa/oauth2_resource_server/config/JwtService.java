package com.nasa.oauth2_resource_server.config;

import com.nasa.oauth2_resource_server.dtos.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    @Qualifier("refreshTokenJwtEncoder")
    private JwtEncoder refreshTokenJwtEncoder;

    private final long expiresInAccessToken;

    private final long expiresInRefreshToken;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.expiresInAccessToken = jwtProperties.getAccessToken().getExpiresIn();

        this.expiresInRefreshToken = jwtProperties.getRefreshToken().getExpiresIn();
    }


    public TokenDTO createToken(String userId, String tokenId) {
         String accessToken = createAccessToken(userId);

        String refreshToken = createRefreshToken(userId, tokenId);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(String userId) {
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(expiresInAccessToken, ChronoUnit.SECONDS))
                .subject(String.valueOf(userId))
                .build();


        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    private String createRefreshToken(String userId, String tokenId) {
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(expiresInRefreshToken, ChronoUnit.SECONDS))
                .subject(userId)
                .id(tokenId)
                .build();

        return refreshTokenJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
