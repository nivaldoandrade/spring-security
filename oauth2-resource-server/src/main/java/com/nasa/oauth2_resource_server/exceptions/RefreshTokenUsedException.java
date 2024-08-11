package com.nasa.oauth2_resource_server.exceptions;

public class RefreshTokenUsedException extends RuntimeException {

    public RefreshTokenUsedException() {
        super("The refresh token has been used");
    }
}
