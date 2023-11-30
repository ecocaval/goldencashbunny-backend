package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.presentation.entities.Account;
import com.goldencashbunny.demo.presentation.exceptions.DuplicationOnRegistrationException;
import com.goldencashbunny.demo.presentation.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountUseCaseImpl implements AccountUseCase {

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountUseCaseImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Account create(Account account) {

        this.encryptAccountPassword(account);

        this.validateDuplicityInRegister(account);

        return this.accountRepository.save(account);
    }

    private void encryptAccountPassword(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
    }

    private void validateDuplicityInRegister(Account account) {

        if (this.foundAccountWithSameEmail(account.getEmail()))
            throw new DuplicationOnRegistrationException(ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_EMAIL.getMessage());

        if (this.foundAccountWithSameCpf(account.getCpf()))
            throw new DuplicationOnRegistrationException(ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_CPF.getMessage());

        if (this.foundAccountWithSameCnpj(account.getCnpj()))
            throw new DuplicationOnRegistrationException(ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_CNPJ.getMessage());
    }

    private boolean foundAccountWithSameEmail(String email) {
        return this.accountRepository.findByEmail(email).isPresent() ? Boolean.TRUE : Boolean.FALSE;
    }

    private boolean foundAccountWithSameCpf(String cpf) {
        return this.accountRepository.findByCpf(cpf).isPresent() ? Boolean.TRUE : Boolean.FALSE;
    }

    private boolean foundAccountWithSameCnpj(String cnpj) {
        return this.accountRepository.findByCnpj(cnpj).isPresent() ? Boolean.TRUE : Boolean.FALSE;
    }

}
