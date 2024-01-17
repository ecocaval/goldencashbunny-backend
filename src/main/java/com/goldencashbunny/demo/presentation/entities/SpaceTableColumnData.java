package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
}
