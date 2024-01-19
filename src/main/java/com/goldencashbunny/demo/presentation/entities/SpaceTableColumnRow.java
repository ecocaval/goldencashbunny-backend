package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateSpaceTableColumnRowRequest;
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
public class SpaceTableColumnRow extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "space_table_column_id")
    private SpaceTableColumn spaceTableColumn;

    @Column(nullable = false)
    private Integer rowReference;

    @Column(nullable = false)
    private String value;

    public SpaceTableColumnRow(SpaceTableColumnRow columnData) {
        super(columnData.getId(), columnData.getCreationDate(), columnData.getLastModifiedDate(), columnData.isDeleted());
        this.spaceTableColumn = columnData.spaceTableColumn;
        this.rowReference = columnData.rowReference;
        this.value = columnData.value;
    }

    public static SpaceTableColumnRow fromCreateSpaceTableColumnRowRequest(
            CreateSpaceTableColumnRowRequest request,
            SpaceTableColumn spaceTableColumn
    ) {
        return SpaceTableColumnRow.builder()
                .spaceTableColumn(spaceTableColumn)
                .rowReference(request.getRowReference())
                .value(request.getValue())
                .creationDate(LocalDateTime.now())
                .build();
    }
}
