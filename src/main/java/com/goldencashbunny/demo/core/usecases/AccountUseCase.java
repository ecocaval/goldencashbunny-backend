package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateAccountRequest;
import com.goldencashbunny.demo.presentation.entities.Account;

public interface AccountUseCase {

    Account findById(String accountId,  boolean shouldThrowException);

    Account findByUserName(String cpf,  boolean shouldThrowException);

    Account findByEmail(String email,  boolean shouldThrowException);

    Account findByCpf(String cpf,  boolean shouldThrowException);

    Account findByCnpj(String cpf,  boolean shouldThrowException);

    Account findByLogin(String login);

    Account create(CreateAccountRequest request);

    Account update(UpdateAccountRequest request, String accountId);
}
