package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.dtos.CreateWorkSpaceDto;
import com.goldencashbunny.demo.core.data.enums.RegexValidator;
import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.core.utils.AsciiUtils;
import com.goldencashbunny.demo.core.utils.UuidUtils;
import com.goldencashbunny.demo.presentation.entities.Account;
import com.goldencashbunny.demo.presentation.entities.Customer;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import com.goldencashbunny.demo.presentation.exceptions.WorkSpaceNotFoundException;
import com.goldencashbunny.demo.presentation.repositories.CustomerRepository;
import com.goldencashbunny.demo.presentation.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WorkSpaceUseCaseImpl implements WorkSpaceUseCase {

    private final AccountUseCase accountUseCase;

    private final WorkspaceRepository workspaceRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public WorkSpaceUseCaseImpl(
        AccountUseCase accountUseCase, WorkspaceRepository workspaceRepository, CustomerRepository customerRepository
    ) {
        this.accountUseCase = accountUseCase;
        this.workspaceRepository = workspaceRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Workspace findById(String workSpaceId) {
        return this.workspaceRepository.findById(
                UuidUtils.getValidUuidFromString(workSpaceId, ErrorMessages.ERROR_INVALID_WORKSPACE_ID.getMessage())
        ).orElseThrow(() -> new WorkSpaceNotFoundException(ErrorMessages.ERROR_WORKSPACE_NOT_FOUND_BY_ID.getMessage()));
    }

    @Override
    public Workspace create(CreateWorkSpaceRequest request, String accountId) {

        var account = this.accountUseCase.findById(accountId, Boolean.TRUE);

        var workSpaceDto = CreateWorkSpaceDto.fromCreateWorkSpaceRequestAndAccount(request, account);

        var workSpace = Workspace.fromCreateWorkSpaceDto(workSpaceDto);

        return this.workspaceRepository.save(workSpace);
    }

    @Override
    public Workspace update(UpdateWorkSpaceRequest request, Workspace nonUpdatedWorkSpace) {
        return this.workspaceRepository.save(Workspace.fromUpdateRequest(request, nonUpdatedWorkSpace));
    }

    @Override
    public void deleteMany(Set<Workspace> workspaces) {
        this.workspaceRepository.deleteAll(workspaces);
    }

    @Override
    public Set<Workspace> findWorkSpacesByAccountId(String accountId) {
        return this.workspaceRepository.findByAccountIdOrderByIsFavoriteDesc(
            UuidUtils.getValidUuidFromString(accountId, ErrorMessages.ERROR_INVALID_ACCOUNT_ID.getMessage())
        );
    }

    @Override
    public Customer createCustomerForWorkspace(CreateCustomerRequest request, Workspace workspace) {

        Customer customer = Customer.fromCreateRequest(request, workspace);

        validateInputs(customer);
        cleanInputs(customer);

        return this.customerRepository.save(customer);
    }

    private void validateInputs(Customer customer) {
        if(customer.getEmail() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.EMAIL_REGEX,
                    customer.getEmail(),
                    ErrorMessages.ERROR_EMAIL_OUT_OF_PATTERN.getMessage()
            );

        if(customer.getCpf() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CPF_REGEX,
                    customer.getCpf(),
                    ErrorMessages.ERROR_CPF_OUT_OF_PATTERN.getMessage()
            );

        if(customer.getCnpj() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CNPJ_REGEX,
                    customer.getCnpj(),
                    ErrorMessages.ERROR_CNPJ_OUT_OF_PATTERN.getMessage()
            );

        if(customer.getCustomerAdditionalEmails() != null)
            customer.getCustomerAdditionalEmails().forEach(email -> {
                RegexValidator.applyRegexValidation(
                        RegexValidator.EMAIL_REGEX,
                        email.getEmail(),
                        ErrorMessages.ERROR_EMAIL_OUT_OF_PATTERN.getMessage()
                );
            });
    }

    private void cleanInputs(Customer customer) {
        customer.setEmail(AsciiUtils.cleanString(customer.getEmail()));
        customer.setCpf(AsciiUtils.cleanDocumentString(customer.getCpf()));
        customer.setCnpj(AsciiUtils.cleanDocumentString(customer.getCnpj()));
    }

}
