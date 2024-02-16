package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateCustomerRequest;
import com.goldencashbunny.demo.presentation.entities.Customer;
import com.goldencashbunny.demo.presentation.entities.Workspace;

public interface CustomerUseCase {

    Customer create(CreateCustomerRequest request, Workspace workspace);

    Customer update(UpdateCustomerRequest request, Customer nonUpdatedCustomer);

    Customer findById(String customerId);

    void delete(Customer customer);
}
