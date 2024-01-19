package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateSpaceTableRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateSpaceTableRequest;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@SQLDelete(sql = "UPDATE space_table SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class SpaceTable extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceTable")
    private List<SpaceTableColumn> columns;

    public SpaceTable(SpaceTable table) {
        super(table.getId(), table.getCreationDate(), table.getLastModifiedDate(), table.isDeleted());
        this.name = table.name;
        this.space = table.space;
        this.columns = table.columns;
    }

    public static SpaceTable fromCreateSpaceTableRequest(CreateSpaceTableRequest request, Space space) {
        return SpaceTable.builder()
                .name(request.getName())
                .space(space)
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static SpaceTable fromUpdateSpaceTableRequest(
            UpdateSpaceTableRequest updateSpaceTableRequest,
            SpaceTable nonUpdatedSpaceTable
    ) {

        var updatedSpaceTable = new SpaceTable(nonUpdatedSpaceTable);

        if(updateSpaceTableRequest.getName() != null) {
            updatedSpaceTable.setName(updateSpaceTableRequest.getName());
        }

        return updatedSpaceTable;
    }
}
