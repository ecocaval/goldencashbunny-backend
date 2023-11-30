package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.Account;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
