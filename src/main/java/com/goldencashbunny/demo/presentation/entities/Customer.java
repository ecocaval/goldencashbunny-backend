package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(length = 11)
    private String cpf;

    @Column(length = 14)
    private String cnpj;

    @Column(nullable = false)
    private String companyName;

    @Column
    private String socialCompanyName;

    @Column
    private String email;

    @Column
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<CustomerAdditionalEmail> customerAdditionalEmails;

    public static Customer fromCreateRequest(CreateCustomerRequest request, Workspace workspace) {
        var customer = Customer.builder()
                .firstName(request.getFirstName())
                .cpf(request.getCpf())
                .cnpj(request.getCnpj())
                .companyName(request.getCompanyName())
                .socialCompanyName(request.getSocialCompanyName())
                .phone(request.getPhone())
                .workspace(workspace)
                .build();

        if(request.getCustomerAdditionalEmails() != null) {
            customer.setCustomerAdditionalEmails(
                    request.getCustomerAdditionalEmails().stream()
                            .map(email -> new CustomerAdditionalEmail(customer, email))
                            .toList()
            );
        }

        if(request.getAddress() != null) {
            customer.setAddress(
                    Address.builder()
                            .zipCode(request.getAddress().getZipCode())
                            .name(request.getAddress().getName())
                            .number(request.getAddress().getNumber())
                            .neighborhood(request.getAddress().getNeighborhood())
                            .complement(request.getAddress().getComplement())
                            .city(request.getAddress().getCity())
                            .state(request.getAddress().getState())
                            .ibgeCode(request.getAddress().getIbgeCode())
                            .customer(customer)
                            .build()
            );
        }

        return customer;
    }

}
