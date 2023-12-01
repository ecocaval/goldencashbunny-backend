package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AccountResponse {

    private String userName;

    private String email;

    private String cpf;

    private String cnpj;

    public static AccountResponse fromAccount(Account account) {
        return AccountResponse.builder()
                .userName(account.getUserName())
                .email(account.getEmail())
                .cpf(account.getCpf())
                .cnpj(account.getCnpj())
                .build();
    }
}
