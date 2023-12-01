package com.goldencashbunny.demo.presentation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class SpaceTable extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceTable")
    private List<SpaceTableColumn> columns;
}
