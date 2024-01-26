package com.goldencashbunny.demo.core.data.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSpaceTableColumnRowRequest {

    @NotNull(message = "O id da coluna não pode ser vazio.")
    private Integer columnReference;

    @NotNull(message = "A referencia da linha da coluna não pode ser vazio.")
    private Integer rowReference;

    @NotNull(message = "O dado da coluna não pode ser vazio.")
    private String value;
}
