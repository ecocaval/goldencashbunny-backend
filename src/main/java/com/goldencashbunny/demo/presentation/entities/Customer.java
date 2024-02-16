package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateCustomerRequest;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@SQLDelete(sql = "UPDATE customer SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
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

    public Customer(Customer customer) {
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.cpf = customer.getCpf();
        this.cnpj = customer.getCnpj();
        this.companyName = customer.getCompanyName();
        this.socialCompanyName = customer.getSocialCompanyName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.address = customer.getAddress();
        this.workspace = customer.getWorkspace();
    }

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

        if (request.getAdditionalEmails() != null) {
            customer.setCustomerAdditionalEmails(
                    request.getAdditionalEmails().stream()
                            .map(email -> new CustomerAdditionalEmail(customer, email))
                            .toList()
            );
        }

        if (request.getAddress() != null) {
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

    public static Customer fromUpdateRequest(UpdateCustomerRequest updateCustomerRequest, Customer nonUpdatedCustomer) {

        var updatedCustomer = new Customer(nonUpdatedCustomer);

        Optional.ofNullable(updateCustomerRequest.getFirstName()).ifPresent(updatedCustomer::setFirstName);

        Optional.ofNullable(updateCustomerRequest.getLastName()).ifPresent(updatedCustomer::setLastName);

        Optional.ofNullable(updateCustomerRequest.getCpf()).ifPresent(updatedCustomer::setCpf);

        Optional.ofNullable(updateCustomerRequest.getCnpj()).ifPresent(updatedCustomer::setCnpj);

        Optional.ofNullable(updateCustomerRequest.getCompanyName()).ifPresent(updatedCustomer::setCompanyName);

        Optional.ofNullable(updateCustomerRequest.getSocialCompanyName()).ifPresent(updatedCustomer::setSocialCompanyName);

        Optional.ofNullable(updateCustomerRequest.getEmail()).ifPresent(updatedCustomer::setEmail);

        Optional.ofNullable(updateCustomerRequest.getPhone()).ifPresent(updatedCustomer::setPhone);

        Optional.ofNullable(updateCustomerRequest.getAddress()).ifPresent(address ->
                updatedCustomer.setAddress(Address.fromUpdateRequest(address, updatedCustomer))
        );

        var updatedCustomerEmails = nonUpdatedCustomer.getCustomerAdditionalEmails();

        updatedCustomer.setCustomerAdditionalEmails(updatedCustomerEmails);

        if (updateCustomerRequest.getAdditionalEmailsToAdd() != null) {

            List<CustomerAdditionalEmail> updatedEmails = updateCustomerRequest.getAdditionalEmailsToAdd().stream()
                    .map(email -> new CustomerAdditionalEmail(nonUpdatedCustomer, email))
                    .collect(Collectors.toList());

            if (updatedCustomerEmails != null) {
                updatedEmails.addAll(updatedCustomerEmails);
            }

            updatedCustomer.setCustomerAdditionalEmails(updatedEmails);
        }

        if(updateCustomerRequest.getAdditionalEmailsToRemove() != null) {
            List<CustomerAdditionalEmail> updatedEmails = updatedCustomer.getCustomerAdditionalEmails().stream()
                    .filter(email -> !updateCustomerRequest.getAdditionalEmailsToRemove().contains(email.getEmail()))
                    .toList();

            updatedCustomer.setCustomerAdditionalEmails(updatedEmails);
        }

        return updatedCustomer;
    }

}
