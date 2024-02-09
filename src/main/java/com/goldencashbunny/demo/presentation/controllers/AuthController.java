package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.AuthenticationRequest;
import com.goldencashbunny.demo.core.data.requests.RefreshTokenAuthenticationRequest;
import com.goldencashbunny.demo.core.data.responses.AuthenticationResponse;
import com.goldencashbunny.demo.core.usecases.AuthenticationUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthenticationUseCase authenticationUseCase;

    @Autowired
    public AuthController(AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok().body(this.authenticationUseCase.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> authenticateByRefreshToken(@RequestBody @Valid RefreshTokenAuthenticationRequest request) {
        return ResponseEntity.ok().body(this.authenticationUseCase.authenticateWithRefreshToken(request));
    }
}
