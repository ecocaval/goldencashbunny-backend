package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class AccountResponse {

    private UUID id;

    private String userName;

    private String email;

    private String cpf;

    private String cnpj;

    public static AccountResponse fromAccount(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userName(account.getUserName())
                .email(account.getEmail())
                .cpf(account.getCpf())
                .cnpj(account.getCnpj())
                .build();
    }
}
