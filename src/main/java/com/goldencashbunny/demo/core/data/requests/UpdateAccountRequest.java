package com.goldencashbunny.demo.core.data.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateAccountRequest {

    private String userName;

    private String email;

    private String cpf;

    private String cnpj;
}
