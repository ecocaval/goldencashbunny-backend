package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateAccountRequest;
import com.goldencashbunny.demo.core.data.responses.AccountResponse;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.utils.JwtUtils;
import com.goldencashbunny.demo.presentation.exceptions.base.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/account")
public class AccountController {

    private final AccountUseCase accountUseCase;

    @Autowired
    public AccountController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    @GetMapping("/id/{accountId}")
    public ResponseEntity<AccountResponse> findById(@PathVariable("accountId") String accountId) {

        if(!JwtUtils.checkRoleOrSameUser(accountId)) {
            throw new UnauthorizedException(ErrorMessages.ERROR_INVALID_TOKEN.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(
            AccountResponse.fromAccount(this.accountUseCase.findById(accountId, Boolean.TRUE))
        );
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody @Valid CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            AccountResponse.fromAccount(this.accountUseCase.create(request))
        );
    }

    @PatchMapping("/id/{accountId}")
    public ResponseEntity<AccountResponse> update(
            @RequestBody UpdateAccountRequest request,
            @PathVariable("accountId") String accountId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            AccountResponse.fromAccount(this.accountUseCase.update(request, accountId))
        );
    }
}
