package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateSpaceTableColumnDataRequest;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SpaceTableColumnData extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "space_table_column_id")
    private SpaceTableColumn spaceTableColumn;

    @Column(nullable = false)
    private Integer rowReference;

    @Column(nullable = false)
    private String value;

    public SpaceTableColumnData(SpaceTableColumnData columnData) {
        super(columnData.getId(), columnData.getCreationDate(), columnData.getLastModifiedDate(), columnData.isDeleted());
        this.spaceTableColumn = columnData.spaceTableColumn;
        this.rowReference = columnData.rowReference;
        this.value = columnData.value;
    }

    public static SpaceTableColumnData fromCreateSpaceTableColumnRequest(
            CreateSpaceTableColumnDataRequest request,
            SpaceTableColumn spaceTableColumn
    ) {
        return SpaceTableColumnData.builder()
                .spaceTableColumn(spaceTableColumn)
                .rowReference(request.getRowReference())
                .value(request.getValue())
                .creationDate(LocalDateTime.now())
                .build();
    }
}
