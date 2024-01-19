package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.SpaceTableColumnRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SpaceTableColumnRowResponse {

    private UUID id;

    private Integer rowReference;

    private String rowValue;

    public static SpaceTableColumnRowResponse fromSpaceTableColumnRow(SpaceTableColumnRow columnRow) {
        return SpaceTableColumnRowResponse.builder()
                .id(columnRow.getId())
                .rowReference(columnRow.getRowReference())
                .rowValue(columnRow.getValue())
                .build();
    }
}
