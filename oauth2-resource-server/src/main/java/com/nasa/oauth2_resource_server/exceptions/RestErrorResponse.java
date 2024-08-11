package com.nasa.oauth2_resource_server.exceptions;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RestErrorResponse(
        int status,

        String message,

        LocalDateTime timestamp
) {
}
