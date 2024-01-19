package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SpaceTableResponse {

    private UUID id;

    private String name;

    private List<SpaceTableColumnResponse> columns = new ArrayList<>();

    public static SpaceTableResponse fromSpaceTable(SpaceTable spaceTable) {
        return SpaceTableResponse.builder()
                .id(spaceTable.getId())
                .name(spaceTable.getName())
                .columns(
                        spaceTable.getColumns() != null ?
                                spaceTable.getColumns()
                                        .stream()
                                        .map(SpaceTableColumnResponse::fromSpaceTableColumn)
                                        .sorted(Comparator.comparing(SpaceTableColumnResponse::getColumnReference))
                                        .toList()
                                : null
                )
                .build();
    }
}
