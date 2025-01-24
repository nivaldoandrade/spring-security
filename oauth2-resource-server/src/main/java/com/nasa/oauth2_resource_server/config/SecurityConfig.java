package com.nasa.oauth2_resource_server.config;

import com.nasa.oauth2_resource_server.converters.JwtToJwtRefreshTokenConvertor;
import com.nasa.oauth2_resource_server.repositories.UserRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RSAPrivateKey accessTokenPrivateKey;

    private final RSAPublicKey accessTokenPublicKey;

    private final RSAPrivateKey refreshTokenPrivateKey;

    private final RSAPublicKey refreshTokenPublicKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtToJwtRefreshTokenConvertor jwtToJwtRefreshTokenConvertor;

    @Autowired
    OAuth2TokenValidator<Jwt> refreshTokenValidator;

    @Autowired
    public SecurityConfig(JwtProperties jwtProperties) {
        JwtProperties.TypeKey accessToken = jwtProperties.getAccessToken();
        JwtProperties.TypeKey refreshToken = jwtProperties.getRefreshToken();

        this.accessTokenPublicKey = accessToken.getPublicKey();
        this.accessTokenPrivateKey = accessToken.getPrivateKey();
        this.refreshTokenPublicKey = refreshToken.getPublicKey();
        this.refreshTokenPrivateKey = refreshToken.getPrivateKey();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/h2-console/**",
                                "/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(accessTokenPublicKey).build();

        OAuth2TokenValidator<Jwt> jwtOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(0)),
                JwtValidators.createDefault()
        );

        jwtDecoder.setJwtValidator(jwtOAuth2TokenValidator);

        return jwtDecoder;
    }

    @Bean
    @Primary
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey
                .Builder(accessTokenPublicKey)
                .privateKey(accessTokenPrivateKey)
                .build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>( new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("refreshTokenJwtDecoder")
    public JwtDecoder refreshTokenJwtDecoder() {
       NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(refreshTokenPublicKey).build();

        OAuth2TokenValidator<Jwt> jwtOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<>(
                // Reduzindo o clock skew para 0, sendo que o padrão é 60 segundos.
                new JwtTimestampValidator(Duration.ofSeconds(0)),
                refreshTokenValidator,
                JwtValidators.createDefault()
        );
        jwtDecoder.setJwtValidator(jwtOAuth2TokenValidator);

        return jwtDecoder;
    }

    @Bean
    @Qualifier("refreshTokenJwtEncoder")
    public JwtEncoder refreshTokenJwtEncoder() {
        JWK jwk = new RSAKey
                .Builder(refreshTokenPublicKey)
                .privateKey(refreshTokenPrivateKey)
                .build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>( new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService());

        return authProvider;
    }

    @Bean
    @Qualifier("refreshTokenAuthenticationProvider")
    public AuthenticationProvider refreshTokenAuthenticationProvider() {
        JwtAuthenticationProvider authProvider = new JwtAuthenticationProvider(refreshTokenJwtDecoder());
        authProvider.setJwtAuthenticationConverter(jwtToJwtRefreshTokenConvertor);

        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not found"));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
