package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateAccountRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

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

    public static Account fromUpdateRequest(UpdateAccountRequest updateAccountRequest) {
        return Account.builder()
                .userName(updateAccountRequest.getUserName())
                .email(updateAccountRequest.getEmail())
                .cpf(updateAccountRequest.getCpf())
                .cnpj(updateAccountRequest.getCnpj())
                .build();
    }

    public static Account complementUpdateInformation(Account account, Account notUpdatedAccount) {
        return Account.builder()
                .id(notUpdatedAccount.getId())
                .creationDate(notUpdatedAccount.getCreationDate())
                .userName(Optional.ofNullable(account.getUserName()).orElse(notUpdatedAccount.getUserName()))
                .email(Optional.ofNullable(account.getEmail()).orElse(notUpdatedAccount.getEmail()))
                .cpf(Optional.ofNullable(account.getCpf()).orElse(notUpdatedAccount.getCpf()))
                .cnpj(Optional.ofNullable(account.getCnpj()).orElse(notUpdatedAccount.getCnpj()))
                .password(notUpdatedAccount.getPassword())
                .roles(notUpdatedAccount.getRoles())
                .build();
    }
}
