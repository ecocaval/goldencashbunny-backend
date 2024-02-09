package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.Address;
import com.goldencashbunny.demo.presentation.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}
