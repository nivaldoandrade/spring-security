package com.nasa.oauth2_resource_server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private TypeKey refreshToken = new TypeKey();
    private TypeKey accessToken = new TypeKey();


    @Getter
    @Setter
    public static class TypeKey {
        private RSAPrivateKey privateKey;

        private RSAPublicKey publicKey;

        private long expiresIn;
    }
}
