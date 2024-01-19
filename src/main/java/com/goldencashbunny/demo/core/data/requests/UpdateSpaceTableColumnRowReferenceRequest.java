package com.goldencashbunny.demo.core.data.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateSpaceTableColumnRowReferenceRequest {

    @NotBlank(message = "A referencia da linha que será atualizada deve ser informada.")
    private Integer from;

    @NotBlank(message = "A referencia da linha que será atualizada deve ser informada.")
    private Integer to;
}
