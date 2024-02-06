package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
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

    @Autowired
    public CustomerController(WorkSpaceUseCase workSpaceUseCase) {
        this.workSpaceUseCase = workSpaceUseCase;
    }

    @PostMapping("/{workSpaceId}")
    public ResponseEntity<?> createCustomerForWorkspace(
            @PathVariable("workSpaceId") String workSpaceId,
            @RequestBody @Valid CreateCustomerRequest request
    ) {
        var workSpace = this.workSpaceUseCase.findById(workSpaceId);

        JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.workSpaceUseCase.createCustomerForWorkspace(request, workSpace)
        );
    }
}
