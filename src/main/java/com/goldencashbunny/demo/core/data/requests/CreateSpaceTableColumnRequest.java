package com.goldencashbunny.demo.core.data.requests;

import com.goldencashbunny.demo.core.data.enums.SpaceTableColumnType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSpaceTableColumnRequest {

    @NotBlank(message = "O nome da coluna não pode ser vazio.")
    private String name;

    @NotBlank(message = "O tipo da coluna não pode ser vazio.")
    private SpaceTableColumnType columnType;
}
