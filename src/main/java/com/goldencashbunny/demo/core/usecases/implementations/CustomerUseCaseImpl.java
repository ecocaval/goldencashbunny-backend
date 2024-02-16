package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.dtos.CreateWorkSpaceDto;
import com.goldencashbunny.demo.core.data.enums.RegexValidator;
import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.usecases.CustomerUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.core.utils.AsciiUtils;
import com.goldencashbunny.demo.core.utils.UuidUtils;
import com.goldencashbunny.demo.presentation.entities.Customer;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import com.goldencashbunny.demo.presentation.exceptions.CustomerNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.WorkSpaceNotFoundException;
import com.goldencashbunny.demo.presentation.repositories.CustomerRepository;
import com.goldencashbunny.demo.presentation.repositories.WorkspaceRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomerUseCaseImpl implements CustomerUseCase {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerUseCaseImpl(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer create(CreateCustomerRequest request, Workspace workspace) {

        Customer customer = Customer.fromCreateRequest(request, workspace);

        validateInputs(customer);
        cleanInputs(customer);

        return this.customerRepository.save(customer);
    }

    @Override
    public Customer update(UpdateCustomerRequest request, Customer nonUpdatedCustomer) {
        return this.customerRepository.save(Customer.fromUpdateRequest(request, nonUpdatedCustomer));
    }


    @Override
    public Customer findById(String customerId) {
        return this.customerRepository.findById(
                UuidUtils.getValidUuidFromString(customerId, ErrorMessages.ERROR_INVALID_CUSTOMER_ID.getMessage())
        ).orElseThrow(() -> new CustomerNotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND_BY_ID.getMessage()));
    }

    @Override
    public void delete(Customer customer) {
        this.customerRepository.delete(customer);
    }

    private void validateInputs(Customer customer) {
        if (customer.getEmail() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.EMAIL_REGEX,
                    customer.getEmail(),
                    ErrorMessages.ERROR_EMAIL_OUT_OF_PATTERN.getMessage()
            );

        if (customer.getCpf() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CPF_REGEX,
                    customer.getCpf(),
                    ErrorMessages.ERROR_CPF_OUT_OF_PATTERN.getMessage()
            );

        if (customer.getCnpj() != null)
            RegexValidator.applyRegexValidation(
                    RegexValidator.CNPJ_REGEX,
                    customer.getCnpj(),
                    ErrorMessages.ERROR_CNPJ_OUT_OF_PATTERN.getMessage()
            );

        if (customer.getCustomerAdditionalEmails() != null)
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
