package com.goldencashbunny.demo.core.usecases.implementations;

import com.auth0.jwt.JWT;
import com.goldencashbunny.demo.core.data.requests.AuthenticationRequest;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.usecases.AuthenticationUseCase;
import com.goldencashbunny.demo.core.utils.DateAndTimeUtils;
import com.goldencashbunny.demo.core.utils.JwtUtils;
import com.goldencashbunny.demo.presentation.entities.Account;
import com.goldencashbunny.demo.presentation.exceptions.InvalidLoginException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class AuthenticationUseCaseImpl implements AuthenticationUseCase {

    private final AccountUseCase accountUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthenticationUseCaseImpl(
        AccountUseCase accountUseCase,
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils
    ) {
        this.accountUseCase = accountUseCase;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String authenticate(AuthenticationRequest request) {

        var account = this.accountUseCase.findByLogin(request.getLogin());

        if(!this.passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new InvalidLoginException();
        }

        return this.generateToken(account, request.getLogin());
    }

    @Override
    public String generateToken(Account account, String login) {
        try {
            return JWT.create()
                    .withIssuer(jwtUtils.APPLICATION_ISSUER)
                    .withSubject(login)
                    .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of(DateAndTimeUtils.BRAZIL_ZONE_OFFSET)))
                    .withClaim("id", account.getId().toString())
                    .withClaim("roles", account.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
                    .withClaim("cpf", account.getCpf())
                    .withClaim("cnpj", account.getCnpj())
                    .sign(jwtUtils.TOKEN_ALGORITHM);

        } catch (Exception exception) {
            throw new RuntimeException(ErrorMessages.ERROR_DURING_TOKEN_GENERATION.getMessage());
        }
    }
}
