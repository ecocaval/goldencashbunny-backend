package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.presentation.entities.Customer;
import com.goldencashbunny.demo.presentation.entities.Workspace;

import java.util.Set;

public interface WorkSpaceUseCase {

    Workspace findById(String workSpaceId);

    Workspace create(CreateWorkSpaceRequest request, String accountId);

    Workspace update(UpdateWorkSpaceRequest request, Workspace nonUpdatedWorkSpace);

    void deleteMany(Set<Workspace> workSpaces);

    Set<Workspace> findWorkSpacesByAccountId(String accountId);

    Customer createCustomerForWorkspace(CreateCustomerRequest request, Workspace workspace);
}
