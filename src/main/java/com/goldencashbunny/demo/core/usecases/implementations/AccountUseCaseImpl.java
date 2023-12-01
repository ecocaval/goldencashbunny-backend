package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.enums.RegexValidator;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.utils.AsciiUtils;
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

    private final AsciiUtils asciiUtils;

    @Autowired
    public AccountUseCaseImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.asciiUtils = new AsciiUtils();
    }

    @Override
    public Account create(Account account) {

        this.cleanCreationInputs(account);

        this.validateCreationInputs(account);

        this.validateDuplicityInRegister(account);

        this.encryptAccountPassword(account);

        return this.accountRepository.save(account);
    }

    private void cleanCreationInputs(Account account) {
        account.setEmail(asciiUtils.cleanString(account.getEmail()));
        account.setCpf(asciiUtils.cleanDocumentString(account.getCpf()));
        account.setCnpj(asciiUtils.cleanDocumentString(account.getCnpj()));
    }

    private void validateCreationInputs(Account account) {
        RegexValidator.applyRegexValidation(
                RegexValidator.EMAIL_REGEX,
                account.getEmail(),
                ErrorMessages.ERROR_ACCOUNT_EMAIL_OUT_OF_PATTERN.getMessage()
        );

        if(account.getCpf() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CPF_REGEX,
                    account.getCpf(),
                    ErrorMessages.ERROR_ACCOUNT_CPF_OUT_OF_PATTERN.getMessage()
            );

        if(account.getCnpj() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CNPJ_REGEX,
                    account.getCnpj(),
                    ErrorMessages.ERROR_ACCOUNT_CNPJ_OUT_OF_PATTERN.getMessage()
            );
    }

    private void encryptAccountPassword(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
    }

    private void validateDuplicityInRegister(Account account) {

        if (this.accountRepository.existsByEmailAndEmailIsNotNull(account.getEmail()))
            throw new DuplicationOnRegistrationException(
                    ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_EMAIL.getMessage(), "email", account.getEmail()
            );

        if (this.accountRepository.existsByCpfAndCpfIsNotNull(account.getCpf()))
            throw new DuplicationOnRegistrationException(
                    ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_CPF.getMessage(), "cpf", account.getCpf()
            );

        if (this.accountRepository.existsByCnpjAndCnpjIsNotNull(account.getCnpj()))
            throw new DuplicationOnRegistrationException(
                    ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_CNPJ.getMessage(), "cnpj", account.getCnpj()
            );
    }

}
