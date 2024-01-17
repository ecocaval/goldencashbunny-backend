package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpaceRepository extends JpaRepository<Space, UUID> {

    List<Space> findByWorkspaceIdOrderByIsFavoriteDesc(UUID workSpaceId);
}
