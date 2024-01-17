package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.enums.SpaceTableColumnType;
import com.goldencashbunny.demo.core.data.requests.CreateSpaceTableColumnRequest;
import com.goldencashbunny.demo.core.data.requests.CreateSpaceTableRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateSpaceTableColumnRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateSpaceTableRequest;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SpaceTableColumn extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "column_reference")
    private Integer columnReference;

    @ManyToOne
    @JoinColumn(name = "space_table_id")
    private SpaceTable spaceTable;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceTableColumn")
    private List<SpaceTableColumnData> spaceTableColumnData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceTableColumnType columnType;

    public SpaceTableColumn(SpaceTableColumn column) {
        super(column.getId(), column.getCreationDate(), column.getLastModifiedDate(), column.isDeleted());
        this.name = column.name;
        this.columnReference = column.columnReference;
        this.spaceTable = column.spaceTable;
        this.spaceTableColumnData = column.spaceTableColumnData;
        this.columnType = column.columnType;
    }

    public static SpaceTableColumn fromCreateSpaceTableColumnRequest(
            CreateSpaceTableColumnRequest request,
            SpaceTable spaceTable
    ) {
        return SpaceTableColumn.builder()
                .name(request.getName())
                .spaceTable(spaceTable)
                .columnType(request.getColumnType())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static SpaceTableColumn fromUpdateSpaceTableColumnRequest(
            UpdateSpaceTableColumnRequest request, SpaceTableColumn nonUpdatedColumn
    ) {
        var updatedColumn = new SpaceTableColumn(nonUpdatedColumn);

        if(request.getName() != null) {
            updatedColumn.setName(request.getName());
        }

        if(request.getColumnReference() != null) {
            updatedColumn.setColumnReference(request.getColumnReference());
        }

        if(request.getColumnType() != null) {
            updatedColumn.setColumnType(request.getColumnType());
        }

        return updatedColumn;
    }
}
