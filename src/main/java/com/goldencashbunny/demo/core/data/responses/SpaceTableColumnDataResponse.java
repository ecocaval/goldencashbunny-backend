package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.SpaceTableColumnData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SpaceTableColumnDataResponse {

    private UUID id;

    private Integer rowReference;

    private String rowValue;

    public static SpaceTableColumnDataResponse fromSpaceTableColumnData(SpaceTableColumnData columnData) {
        return SpaceTableColumnDataResponse.builder()
                .id(columnData.getId())
                .rowReference(columnData.getRowReference())
                .rowValue(columnData.getValue())
                .build();
    }
}
