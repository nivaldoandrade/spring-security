package com.nasa.oauth2_resource_server.dtos;

import com.nasa.oauth2_resource_server.entities.User;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserDTO(
        UUID id,

        String name,

        String email
) {

    public static UserDTO toResponse(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDTO> toResponseAllUsers(List<User> users) {
        return users.stream().map(UserDTO::toResponse).toList();
    }
}
