package com.nasa.oauth2_resource_server.controllers;

import com.nasa.oauth2_resource_server.dtos.LoginDTO;
import com.nasa.oauth2_resource_server.dtos.RefreshTokenDTO;
import com.nasa.oauth2_resource_server.dtos.TokenDTO;
import com.nasa.oauth2_resource_server.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO token = authService.login(loginDTO);

        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {

        TokenDTO token = authService.refreshToken(refreshTokenDTO);

        return ResponseEntity.ok().body(token);
    }

}

