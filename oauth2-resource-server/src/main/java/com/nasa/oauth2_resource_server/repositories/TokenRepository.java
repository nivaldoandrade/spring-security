package com.nasa.oauth2_resource_server.repositories;

import com.nasa.oauth2_resource_server.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    void deleteAllByUserId(UUID userId);
}
