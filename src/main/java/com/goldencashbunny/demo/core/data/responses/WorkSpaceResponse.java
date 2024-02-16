package com.goldencashbunny.demo.core.data.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkSpaceResponse {

    private UUID id;

    private String companyName;

    private String socialCompanyName;

    private boolean isFavorite;

    public static WorkSpaceResponse fromWorkSpace(Workspace workSpace) {
        return WorkSpaceResponse.builder()
                .id(workSpace.getId())
                .companyName(workSpace.getCompanyName())
                .socialCompanyName(workSpace.getSocialCompanyName())
                .isFavorite(workSpace.isFavorite())
                .build();
    }
}
