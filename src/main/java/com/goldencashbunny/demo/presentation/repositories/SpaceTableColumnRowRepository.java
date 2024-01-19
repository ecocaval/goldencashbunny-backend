package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.SpaceTableColumnRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpaceTableColumnRowRepository extends JpaRepository<SpaceTableColumnRow, UUID> {

    boolean existsByRowReferenceAndSpaceTableColumnId(Integer rowReference, UUID spaceTableColumnId);
}
