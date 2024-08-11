package com.nasa.oauth2_resource_server.services;

import com.nasa.oauth2_resource_server.config.JwtService;
import com.nasa.oauth2_resource_server.dtos.JwtRefreshTokenDTO;
import com.nasa.oauth2_resource_server.dtos.LoginDTO;
import com.nasa.oauth2_resource_server.dtos.RefreshTokenDTO;
import com.nasa.oauth2_resource_server.dtos.TokenDTO;
import com.nasa.oauth2_resource_server.entities.Token;
import com.nasa.oauth2_resource_server.entities.User;
import com.nasa.oauth2_resource_server.repositories.TokenRepository;
import com.nasa.oauth2_resource_server.exceptions.RefreshTokenUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    @Qualifier("refreshTokenAuthenticationProvider")
    private AuthenticationProvider refreshTokenAuthenticationProvider;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    public TokenDTO login(LoginDTO loginDTO) {

        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.password()
                )
        );

        User user = (User) authentication.getPrincipal();

        String userId = user.getId().toString();

        String tokenId = getTokenId(user);

        return jwtService.createToken(userId, tokenId);
    }

    @Transactional(noRollbackFor = {RefreshTokenUsedException.class, AuthenticationException.class})
    public TokenDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        // Foi criado o ExpiredRefreshTokenValidator para realizar a validação do código comentado abaixo
//        try {
//            JWTClaimsSet jwt =  JWTParser.parse(refreshTokenDTO.refreshToken()).getJWTClaimsSet();
//
//            UUID tokenId = UUID.fromString(jwt.getJWTID());
//            boolean hasExpired =  jwt.getExpirationTime().before(new Date());
//
//
//            Optional<Token> token = tokenRepository.findById(tokenId);
//
//            if(token.isPresent() && hasExpired) {
//                tokenRepository.deleteById(tokenId);
//            }
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        Authentication authentication = refreshTokenAuthenticationProvider.authenticate(
                new BearerTokenAuthenticationToken(refreshTokenDTO.refreshToken())
        );

        JwtRefreshTokenDTO jwtRefreshTokenDTO = (JwtRefreshTokenDTO) authentication.getPrincipal();

        String userId = jwtRefreshTokenDTO.userId();
        String tokenId = jwtRefreshTokenDTO.tokenId();

        Optional<Token> refreshTokenExists = tokenRepository.findById(UUID.fromString(tokenId));

        if(refreshTokenExists.isEmpty()) {
            tokenRepository.deleteAllByUserId(UUID.fromString(userId));
            throw new RefreshTokenUsedException();
        }

        User user = refreshTokenExists.get().getUser();

        tokenId = getTokenId(user);

        tokenRepository.delete(refreshTokenExists.get());

        return jwtService.createToken(userId, tokenId);
    }

    private String getTokenId(User user) {
        Token tokenCreate = new Token();
        tokenCreate.setUser(user);

        return tokenRepository.save(tokenCreate).getId().toString();
    }
}
