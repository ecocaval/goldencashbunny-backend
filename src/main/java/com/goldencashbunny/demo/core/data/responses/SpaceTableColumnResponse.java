package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumnData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private List<SpaceTableColumnData> data;

    private String columnType;

    public static SpaceTableColumnResponse fromSpaceTableColumn(SpaceTableColumn column) {
        return SpaceTableColumnResponse.builder()
                .id(column.getId())
                .name(column.getName())
                .columnReference(column.getColumnReference())
                .data(column.getSpaceTableColumnData())
                .columnType(column.getColumnType().name())
                .build();
    }
}
