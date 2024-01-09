package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.core.data.enums.AccountRole;
import com.goldencashbunny.demo.presentation.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(AccountRole name);
}
