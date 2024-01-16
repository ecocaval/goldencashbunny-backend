package com.goldencashbunny.demo.presentation.entities;

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
public class SpaceTable extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceTable")
    private List<SpaceTableColumn> columns;
}
