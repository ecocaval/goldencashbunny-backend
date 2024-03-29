package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.dtos.CreateWorkSpaceDto;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
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
@ToString
@SQLDelete(sql = "UPDATE workspace SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Workspace extends BaseEntity {

    @Column(nullable = false)
    private String companyName;

    @Column
    private String socialCompanyName;

    @Column(columnDefinition = "bool default FALSE", nullable = false)
    private boolean isFavorite;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspace")
    @ToString.Exclude
    private List<Space> spaces;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspace")
    private List<Customer> customers;

    public Workspace(Workspace workspace) {
        this.companyName = workspace.getCompanyName();
        this.socialCompanyName = workspace.getSocialCompanyName();
        this.isFavorite = workspace.isFavorite();
        this.account = workspace.getAccount();
        this.spaces = workspace.getSpaces();
        this.customers = workspace.getCustomers();
    }

    public static Workspace fromCreateWorkSpaceDto(CreateWorkSpaceDto createWorkSpaceDto) {
        return Workspace.builder()
                .companyName(createWorkSpaceDto.getCompanyName())
                .socialCompanyName(createWorkSpaceDto.getSocialCompanyName())
                .account(createWorkSpaceDto.getAccount())
                .deleted(Boolean.FALSE)
                .build();
    }

    public static Workspace fromUpdateRequest(UpdateWorkSpaceRequest updateWorkSpaceRequest, Workspace nonUpdatedWorkSpace) {

        var updatedWorkSpace = new Workspace(nonUpdatedWorkSpace);

        if(updateWorkSpaceRequest.getCompanyName() != null) {
            updatedWorkSpace.setCompanyName(updateWorkSpaceRequest.getCompanyName());
        }

        if(updateWorkSpaceRequest.getSocialCompanyName() != null) {
            updatedWorkSpace.setSocialCompanyName(updateWorkSpaceRequest.getSocialCompanyName());
        }

        if(updateWorkSpaceRequest.getIsFavorite() != null) {
            updatedWorkSpace.setFavorite(updateWorkSpaceRequest.getIsFavorite());
        }

        return updatedWorkSpace;
    }
}
