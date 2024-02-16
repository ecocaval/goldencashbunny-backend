package com.goldencashbunny.demo.core.data.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goldencashbunny.demo.presentation.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String cpf;

    private String cnpj;

    private String companyName;

    private String socialCompanyName;

    private String email;

    private String phone;

    private AddressResponse address;

    private WorkSpaceResponse workspace;

    private List<String> additionalEmails;

    public static CustomerResponse fromCustomer(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .cpf(customer.getCpf())
                .cnpj(customer.getCnpj())
                .companyName(customer.getCompanyName())
                .socialCompanyName(customer.getSocialCompanyName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(AddressResponse.fromAddress(customer.getAddress()))
                .workspace(WorkSpaceResponse.fromWorkSpace(customer.getWorkspace()))
                .additionalEmails(customer.getCustomerAdditionalEmails() != null ?
                        customer.getCustomerAdditionalEmails().stream().map(CustomerAdditionalEmail::getEmail).toList() :
                        null)
                .build();
    }
}
