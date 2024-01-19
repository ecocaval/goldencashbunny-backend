package com.goldencashbunny.demo.core.data.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSpaceTableColumnRowRequest {

    @NotBlank(message = "A referencia da linha da coluna não pode ser vazio.")
    private Integer rowReference;

    @NotBlank(message = "O dado da coluna não pode ser vazio.")
    private String value;
}
