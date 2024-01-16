package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.enums.SpaceTableColumnType;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne
    @JoinColumn(name = "space_table_id")
    private SpaceTable spaceTable;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceTableColumn")
    private List<SpaceTableColumnData> spaceTableColumnData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceTableColumnType columnType;
}
