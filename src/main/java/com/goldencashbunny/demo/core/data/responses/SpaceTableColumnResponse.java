package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SpaceTableColumnResponse {

    private UUID id;

    private String name;

    private Integer columnReference;

    private String columnType;

    private List<SpaceTableColumnRowResponse> rows;

    public static SpaceTableColumnResponse fromSpaceTableColumn(SpaceTableColumn column) {
        return SpaceTableColumnResponse.builder()
                .id(column.getId())
                .name(column.getName())
                .columnReference(column.getColumnReference())
                .columnType(column.getColumnType().name())
                .rows(
                        column.getSpaceTableColumnRows() != null ?
                                column.getSpaceTableColumnRows()
                                        .stream()
                                        .map(SpaceTableColumnRowResponse::fromSpaceTableColumnRow)
                                        .sorted(Comparator.comparing(SpaceTableColumnRowResponse::getRowReference))
                                        .toList()
                                : null
                )
                .build();
    }
}
