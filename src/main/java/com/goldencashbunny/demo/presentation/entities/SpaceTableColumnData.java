package com.goldencashbunny.demo.presentation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class SpaceTableColumnData extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "space_table_column_id")
    private SpaceTableColumn spaceTableColumn;

    @Column(nullable = false)
    private UUID spaceTableRowReference;

    @Column(nullable = false)
    private String value;
}
