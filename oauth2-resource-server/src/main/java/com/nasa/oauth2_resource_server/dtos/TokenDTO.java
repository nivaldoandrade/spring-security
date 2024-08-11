package com.nasa.oauth2_resource_server.dtos;

import lombok.Builder;

@Builder
public record TokenDTO(
        String accessToken,

        String refreshToken
) {
}
