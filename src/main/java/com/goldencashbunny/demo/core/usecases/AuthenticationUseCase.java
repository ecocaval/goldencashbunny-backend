package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.AuthenticationRequest;
import com.goldencashbunny.demo.core.data.requests.RefreshTokenAuthenticationRequest;
import com.goldencashbunny.demo.core.data.responses.AuthenticationResponse;
import com.goldencashbunny.demo.presentation.entities.RefreshToken;

import java.util.UUID;

public interface AuthenticationUseCase {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse authenticateWithRefreshToken(RefreshTokenAuthenticationRequest request);

    RefreshToken findRefreshToken(UUID refreshToken);

}
