package com.goldencashbunny.demo.core.usecases.implementations;

import com.auth0.jwt.JWT;
import com.goldencashbunny.demo.core.data.requests.AuthenticationRequest;
import com.goldencashbunny.demo.core.data.requests.RefreshTokenAuthenticationRequest;
import com.goldencashbunny.demo.core.data.responses.AuthenticationResponse;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.usecases.AuthenticationUseCase;
import com.goldencashbunny.demo.core.utils.DateAndTimeUtils;
import com.goldencashbunny.demo.infra.security.JwtUtils;
import com.goldencashbunny.demo.presentation.entities.Account;
import com.goldencashbunny.demo.presentation.entities.RefreshToken;
import com.goldencashbunny.demo.presentation.entities.Role;
import com.goldencashbunny.demo.presentation.exceptions.InvalidLoginException;
import com.goldencashbunny.demo.presentation.exceptions.RefreshTokenNotFoundException;
import com.goldencashbunny.demo.presentation.repositories.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationUseCaseImpl implements AuthenticationUseCase {

    private final AccountUseCase accountUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationUseCaseImpl(
        AccountUseCase accountUseCase,
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils,
        RefreshTokenRepository refreshTokenRepository
    ) {
        this.accountUseCase = accountUseCase;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var account = this.accountUseCase.findByLogin(request.getLogin());

        if(!this.passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new InvalidLoginException();
        }

        var token = this.generateToken(account, request.getLogin());

        var refreshToken = this.refreshTokenRepository.save(RefreshToken.fromAccount(account));

        return new AuthenticationResponse(token, refreshToken.getId());
    }

    @Override
    @Transactional
    public AuthenticationResponse authenticateWithRefreshToken(RefreshTokenAuthenticationRequest request) {

        var refreshToken = this.findRefreshToken(request.getRefreshToken());

        var token = this.generateToken(refreshToken.getAccount());

        refreshToken.setLastModifiedDate(LocalDateTime.now());

        this.refreshTokenRepository.delete(refreshToken);

        var newRefreshToken = this.refreshTokenRepository.save(RefreshToken.fromAccount(refreshToken.getAccount()));

        return new AuthenticationResponse(token, newRefreshToken.getId());
    }

    @Override
    public RefreshToken findRefreshToken(UUID refreshToken) {
        return this.refreshTokenRepository.findById(refreshToken).orElseThrow(
            () -> new RefreshTokenNotFoundException(ErrorMessages.ERROR_REFRESH_TOKEN_NOT_FOUND_BY_ID.getMessage())
        );
    }

    private String generateToken(Account account, String login) {
        try {
            return JWT.create()
                    .withIssuer(jwtUtils.APPLICATION_ISSUER)
                    .withSubject(login)
                    .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of(DateAndTimeUtils.BRAZIL_ZONE_OFFSET)))
                    .withClaim("id", account.getId().toString())
                    .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .withClaim("cpf", account.getCpf())
                    .withClaim("cnpj", account.getCnpj())
                    .sign(jwtUtils.TOKEN_ALGORITHM);

        } catch (Exception exception) {
            throw new RuntimeException(ErrorMessages.ERROR_DURING_TOKEN_GENERATION.getMessage());
        }
    }

    private String generateToken(Account account) {
        try {
            return JWT.create()
                    .withIssuer(jwtUtils.APPLICATION_ISSUER)
                    .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of(DateAndTimeUtils.BRAZIL_ZONE_OFFSET)))
                    .withClaim("id", account.getId().toString())
                    .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .withClaim("cpf", account.getCpf())
                    .withClaim("cnpj", account.getCnpj())
                    .sign(jwtUtils.TOKEN_ALGORITHM);

        } catch (Exception exception) {
            throw new RuntimeException(ErrorMessages.ERROR_DURING_TOKEN_GENERATION.getMessage());
        }
    }
}
