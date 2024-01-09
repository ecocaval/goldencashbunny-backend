package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.AuthenticationRequest;
import com.goldencashbunny.demo.presentation.entities.Account;

public interface AuthenticationUseCase {

    String authenticate(AuthenticationRequest request);

    String generateToken(Account account, String login);

}
