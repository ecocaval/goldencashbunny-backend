package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpaceTableColumnRepository extends JpaRepository<SpaceTableColumn, UUID> {

    @Query("SELECT MAX(s.columnReference) FROM SpaceTableColumn s WHERE s.spaceTable = :spaceTable")
    Integer findMaxValueOfColumnReference(@Param("spaceTableId") SpaceTable spaceTable);

    SpaceTableColumn findByColumnReference(Integer columnReference);

    List<SpaceTableColumn> findBySpaceTableIdAndColumnReferenceNotIn(UUID spaceTableId, Collection<Integer> columnReference);
}
