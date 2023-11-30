package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Account extends BaseEntity {

    @Column(length = 50, nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 11)
    private String cpf;

    @Column(length = 14)
    private String cnpj;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Workspace> workspaces;

    public static Account fromCreateRequest(CreateAccountRequest createAccountRequest) {
        return Account.builder()
                .id(UUID.randomUUID())
                .creationDate(LocalDateTime.now())
                .userName(createAccountRequest.getUserName())
                .email(createAccountRequest.getEmail())
                .cpf(createAccountRequest.getCpf())
                .cnpj(createAccountRequest.getCnpj())
                .password(createAccountRequest.getPassword())
                .build();
    }
}
