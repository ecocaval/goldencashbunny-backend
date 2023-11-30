package com.goldencashbunny.demo.core.data.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateAccountRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String email;

    private String cpf;

    private String cnpj;

    @NotBlank
    private String password;
}
