package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface SpaceTableRepository extends JpaRepository<SpaceTable, UUID> {

    Set<SpaceTable> findBySpaceId(UUID spaceId);
}
