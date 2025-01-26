package com.nasa.oauth2_resource_server.controllers;

import com.nasa.oauth2_resource_server.dtos.UserDTO;
import com.nasa.oauth2_resource_server.entities.User;
import com.nasa.oauth2_resource_server.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> list() {
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(UserDTO.toResponseAllUsers(users));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        UUID userId = UUID.fromString(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User is not found"));

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok().body(userDTO);
    }
}
