package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateSpaceRequest;
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
@SQLDelete(sql = "UPDATE space SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Space extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "bool default FALSE", nullable = false)
    private boolean isFavorite;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "space")
    private List<SpaceTable> spaceTables;

    public Space(Space space) {
        super(space.getId(), space.getCreationDate(), space.getLastModifiedDate(), space.isDeleted());
        this.name = space.name;
        this.isFavorite = space.isFavorite;
        this.workspace = space.workspace;
        this.spaceTables = space.spaceTables;
    }

    public static Space fromCreateSpaceRequest(CreateSpaceRequest createSpaceRequest, Workspace workspace) {
        return Space.builder()
                .name(createSpaceRequest.getName())
                .workspace(workspace)
                .creationDate(LocalDateTime.now())
                .deleted(Boolean.FALSE)
                .build();
    }

    public static Space fromUpdateSpaceRequest(UpdateSpaceRequest updateSpaceRequest, Space nonUpdatedSpace) {

        var updatedSpace = new Space(nonUpdatedSpace);

        if(updateSpaceRequest.getName() != null) {
            updatedSpace.setName(updateSpaceRequest.getName());
        }

        if(updateSpaceRequest.getIsFavorite() != null) {
            updatedSpace.setFavorite(updateSpaceRequest.getIsFavorite());
        }

        return updatedSpace;
    }
}
