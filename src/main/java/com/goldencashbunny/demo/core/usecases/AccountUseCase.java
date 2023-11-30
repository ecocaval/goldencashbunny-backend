package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.presentation.entities.Account;

public interface AccountUseCase {
    Account create(Account account);
}
