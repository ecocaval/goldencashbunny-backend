package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.dtos.CreateWorkSpaceDto;
import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.core.utils.UuidUtils;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import com.goldencashbunny.demo.presentation.exceptions.WorkSpaceNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import com.goldencashbunny.demo.presentation.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkSpaceUseCaseImpl implements WorkSpaceUseCase {

    private final AccountUseCase accountUseCase;

    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkSpaceUseCaseImpl(
        AccountUseCase accountUseCase, WorkspaceRepository workspaceRepository
    ) {
        this.accountUseCase = accountUseCase;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Workspace findById(String workSpaceId) {
        return this.workspaceRepository.findById(
                UuidUtils.getValidUuidFromString(workSpaceId, "O id do workSpace é inválido.")
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
    public void delete(Workspace workspace) {
        this.workspaceRepository.delete(workspace);
    }

    @Override
    public List<Workspace> findWorkSpacesByAccountId(String accountId) {
        return this.workspaceRepository.findByAccountId(
            UuidUtils.getValidUuidFromString(accountId, "O id da conta é inválido.")
        );
    }

}
