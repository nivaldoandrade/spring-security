package com.nasa.oauth2_resource_server.converters;

import com.nasa.oauth2_resource_server.dtos.JwtRefreshTokenDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtToJwtRefreshTokenConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        JwtRefreshTokenDTO jwtRefreshTokenDTO = JwtRefreshTokenDTO.builder()
                .userId(source.getSubject())
                .tokenId(source.getId())
                .build();


        return new UsernamePasswordAuthenticationToken(
                jwtRefreshTokenDTO,
                source,
                Collections.EMPTY_LIST
        );
    }
}
