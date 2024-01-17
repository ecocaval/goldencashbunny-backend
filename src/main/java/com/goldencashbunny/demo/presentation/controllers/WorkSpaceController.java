package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.responses.DeleteWorkSpacesResponse;
import com.goldencashbunny.demo.core.data.responses.WorkSpaceResponse;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.infra.security.JwtUtils;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import com.goldencashbunny.demo.presentation.exceptions.WorkSpaceNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import com.goldencashbunny.demo.presentation.exceptions.base.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("v1/workspace")
public class WorkSpaceController {

    private final WorkSpaceUseCase workSpaceUseCase;

    @Autowired
    public WorkSpaceController(WorkSpaceUseCase workSpaceUseCase) {
        this.workSpaceUseCase = workSpaceUseCase;
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<WorkSpaceResponse>> findByAccountId(@PathVariable("accountId") String accountId) {

        JwtUtils.validateAdminRoleOrSameAccount(accountId);

        var workSpaces = workSpaceUseCase.findWorkSpacesByAccountId(accountId);

        return ResponseEntity.status(HttpStatus.OK).body(
                workSpaces.stream()
                        .map(WorkSpaceResponse::fromWorkSpace)
                        .toList()
        );
    }

    @PostMapping("/account/{accountId}")
    public ResponseEntity<WorkSpaceResponse> create(
            @PathVariable("accountId") String accountId,
            @RequestBody @Valid CreateWorkSpaceRequest request
    ) {
        JwtUtils.validateAdminRoleOrSameAccount(accountId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            WorkSpaceResponse.fromWorkSpace(this.workSpaceUseCase.create(request, accountId))
        );
    }

    @PatchMapping("/{workSpaceId}")
    public ResponseEntity<WorkSpaceResponse> update(
            @PathVariable("workSpaceId") String workSpaceId,
            @RequestBody @Valid UpdateWorkSpaceRequest request
    ) {

        var workSpace = this.workSpaceUseCase.findById(workSpaceId);

        JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());

        return ResponseEntity.status(HttpStatus.OK).body(
            WorkSpaceResponse.fromWorkSpace(this.workSpaceUseCase.update(request, workSpace))
        );
    }

    @DeleteMapping("/{workSpaceIds}")
    public ResponseEntity<DeleteWorkSpacesResponse> deleteMany(@PathVariable("workSpaceIds") List<String> workSpaceIds) {

        var response = new DeleteWorkSpacesResponse();

        Set<Workspace> workSpacesToDelete = new HashSet<>();

        for (String workSpaceId : workSpaceIds) {

            Workspace workSpace = null;

            try {
                workSpace = this.workSpaceUseCase.findById(workSpaceId);
            } catch (WorkSpaceNotFoundException | BadRequestException ex) {
                response.getErrorMessages().add(
                    new DeleteWorkSpacesResponse.DeleteWorkSpaceErrorMessage(workSpaceId, ex.getMessage())
                );
                continue;
            }

            try {
                JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());
            } catch (UnauthorizedException ex) {
                response.getErrorMessages().add(
                    new DeleteWorkSpacesResponse.DeleteWorkSpaceErrorMessage(workSpaceId, ex.getMessage())
                );
                continue;
            }

            workSpacesToDelete.add(workSpace);
        }

        this.workSpaceUseCase.deleteMany(workSpacesToDelete);

        return ResponseEntity.ok(response);
    }
}
