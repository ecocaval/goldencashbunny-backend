package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.presentation.entities.Workspace;

import java.util.List;

public interface WorkSpaceUseCase {

    Workspace findById(String workSpaceId);

    Workspace create(CreateWorkSpaceRequest request, String accountId);

    Workspace update(UpdateWorkSpaceRequest request, Workspace nonUpdatedWorkSpace);

    void delete(Workspace workSpace);

    List<Workspace> findWorkSpacesByAccountId(String accountId);
}
