package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.enums.AccountRole;
import com.goldencashbunny.demo.core.data.enums.LoginIdentification;
import com.goldencashbunny.demo.core.data.enums.RegexValidator;
import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateAccountRequest;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.utils.AsciiUtils;
import com.goldencashbunny.demo.presentation.entities.Account;
import com.goldencashbunny.demo.presentation.exceptions.AccountNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.DuplicationOnRegistrationException;
import com.goldencashbunny.demo.presentation.exceptions.RoleNotFoundException;
import com.goldencashbunny.demo.presentation.repositories.AccountRepository;
import com.goldencashbunny.demo.presentation.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountUseCaseImpl implements AccountUseCase {

    private final AccountRepository accountRepository;

    private final RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountUseCaseImpl(
        AccountRepository accountRepository, RoleRepository roleRepository
    ) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account findById(String accountId, boolean shouldThrowException) {
        var account = this.accountRepository.findById(UUID.fromString(accountId));

        if(account.isEmpty() && shouldThrowException) {
            throw new AccountNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND_BY_ID.getMessage());
        }

        return account.orElse(null);
    }

    @Override
    public Account findByUserName(String userName, boolean shouldThrowException) {
        var account = this.accountRepository.findByUserName(userName);

        if(account.isEmpty() && shouldThrowException) {
            throw new AccountNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND_BY_USERNAME.getMessage());
        }

        return account.orElse(null);
    }

    @Override
    public Account findByEmail(String email, boolean shouldThrowException) {
        var account = this.accountRepository.findByEmail(email);

        if(account.isEmpty() && shouldThrowException) {
            throw new AccountNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND_BY_EMAIL.getMessage());
        }

        return account.orElse(null);
    }

    @Override
    public Account findByCpf(String cpf, boolean shouldThrowException) {
        var account = this.accountRepository.findByCpf(AsciiUtils.cleanDocumentString(cpf));

        if(account.isEmpty() && shouldThrowException) {
            throw new AccountNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND_BY_CPF.getMessage());
        }

        return account.orElse(null);
    }

    @Override
    public Account findByCnpj(String cnpj, boolean shouldThrowException) {
        var account = this.accountRepository.findByCnpjAndDeletedFalse(AsciiUtils.cleanDocumentString(cnpj));

        if(account.isEmpty() && shouldThrowException) {
            throw new AccountNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND_BY_CNPJ.getMessage());
        }

        return account.orElse(null);
    }

    @Override
    public Account findByLogin(String login) {

        return switch (LoginIdentification.getFromLogin(login)) {
            case USERNAME -> this.findByUserName(login, Boolean.TRUE);
            case EMAIL -> this.findByEmail(login, Boolean.TRUE);
            case CPF -> this.findByCpf(login, Boolean.TRUE);
            case CNPJ -> this.findByCnpj(login, Boolean.TRUE);
        };
    }

    @Override
    public Account create(CreateAccountRequest request) {

        var account = Account.fromCreateRequest(request);

        this.cleanInputs(account);

        this.validateInputs(account);

        this.validateDuplicity(account);

        this.encryptAccountPassword(account);

        var role = this.roleRepository.findByName(AccountRole.USER).orElseThrow(RoleNotFoundException::new);

        account.setRoles(List.of(role));

        return this.accountRepository.save(account);
    }

    @Override
    public Account update(UpdateAccountRequest request, String accountId) {

        var notUpdatedAccount = this.findById(accountId, Boolean.TRUE);

        var account = Account.fromUpdateRequest(request);

        this.cleanInputs(account);

        this.validateInputs(account);

        this.validateDuplicity(account);

        account = Account.complementUpdateInformation(account, notUpdatedAccount);

        return this.accountRepository.save(account);
    }

    private void cleanInputs(Account account) {
        account.setEmail(AsciiUtils.cleanString(account.getEmail()));
        account.setCpf(AsciiUtils.cleanDocumentString(account.getCpf()));
        account.setCnpj(AsciiUtils.cleanDocumentString(account.getCnpj()));
    }

    private void validateInputs(Account account) {
        if(account.getEmail() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.EMAIL_REGEX,
                    account.getEmail(),
                    ErrorMessages.ERROR_EMAIL_OUT_OF_PATTERN.getMessage()
            );

        if(account.getCpf() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CPF_REGEX,
                    account.getCpf(),
                    ErrorMessages.ERROR_CPF_OUT_OF_PATTERN.getMessage()
            );

        if(account.getCnpj() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CNPJ_REGEX,
                    account.getCnpj(),
                    ErrorMessages.ERROR_CNPJ_OUT_OF_PATTERN.getMessage()
            );
    }

    private void encryptAccountPassword(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
    }

    private void validateDuplicity(Account account) {

        if (this.accountRepository.existsByUserNameAndUserNameIsNotNull(account.getUserName()))
            throw new DuplicationOnRegistrationException(
                    ErrorMessages.ERROR_ACCOUNT_DUPLICATED_BY_USER_NAME.getMessage(), "username", account.getUserName()
            );

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
