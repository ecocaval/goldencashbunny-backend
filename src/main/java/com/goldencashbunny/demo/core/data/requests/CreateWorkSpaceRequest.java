package com.goldencashbunny.demo.core.data.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateWorkSpaceRequest {

    @NotBlank(message = "O nome da compania não pode ser vazio")
    private String companyName;

    private String socialCompanyName;
}
