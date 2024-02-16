package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateCustomerRequest;
import com.goldencashbunny.demo.core.data.responses.CustomerResponse;
import com.goldencashbunny.demo.core.usecases.CustomerUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.infra.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/customer")
public class CustomerController {

    private final WorkSpaceUseCase workSpaceUseCase;
    private final CustomerUseCase customerUseCase;

    @Autowired
    public CustomerController(WorkSpaceUseCase workSpaceUseCase, CustomerUseCase customerUseCase) {
        this.workSpaceUseCase = workSpaceUseCase;
        this.customerUseCase = customerUseCase;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable("customerId") String customerId
    ) {
        var customer = this.customerUseCase.findById(customerId);

        JwtUtils.validateAdminRoleOrSameAccount(customer.getWorkspace().getAccount().getId());

        return ResponseEntity.ok(CustomerResponse.fromCustomer(customer));
    }

    @PostMapping("/workspace/{workSpaceId}")
    public ResponseEntity<CustomerResponse> createCustomerForWorkspace(
            @PathVariable("workSpaceId") String workSpaceId,
            @RequestBody @Valid CreateCustomerRequest request
    ) {
        var workSpace = this.workSpaceUseCase.findById(workSpaceId);

        JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CustomerResponse.fromCustomer(customerUseCase.create(request, workSpace))
        );
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable("customerId") String customerId,
            @RequestBody UpdateCustomerRequest request
    ) {
        var customer = this.customerUseCase.findById(customerId);

        JwtUtils.validateAdminRoleOrSameAccount(customer.getWorkspace().getAccount().getId());

        return ResponseEntity.ok(CustomerResponse.fromCustomer(this.customerUseCase.update(request, customer)));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") String customerId) {

        var customer = this.customerUseCase.findById(customerId);

        JwtUtils.validateAdminRoleOrSameAccount(customer.getWorkspace().getAccount().getId());

        this.customerUseCase.delete(customer);

        return ResponseEntity.ok().build();
    }
}
