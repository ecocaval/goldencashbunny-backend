package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.Space;
import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class SpaceResponse {

    private UUID id;

    private String name;

    private boolean isFavorite;

    private List<SpaceTableResponse> spaceTables;

    public static SpaceResponse fromSpace(Space space) {
        return SpaceResponse.builder()
                .id(space.getId())
                .name(space.getName())
                .isFavorite(space.isFavorite())
                .spaceTables(space.getSpaceTables() != null ? space.getSpaceTables().stream().map(SpaceTableResponse::fromSpaceTable).toList() : null)
                .build();
    }
}
