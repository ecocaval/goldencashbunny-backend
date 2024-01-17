package com.goldencashbunny.demo.core.data.requests;

import com.goldencashbunny.demo.core.data.enums.SpaceTableColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateSpaceTableColumnRequest {

    private String name;

    private Integer columnReference;

    private SpaceTableColumnType columnType;
}
