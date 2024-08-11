package com.nasa.oauth2_resource_server.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDTO(
        UUID id,

        String name,

        String email
) {
}
