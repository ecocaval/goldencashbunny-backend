package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.responses.WorkSpaceResponse;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.infra.security.JwtUtils;
import com.goldencashbunny.demo.presentation.exceptions.base.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/workspace")
public class WorkSpaceController {

    private final WorkSpaceUseCase workSpaceUseCase;

    @Autowired
    public WorkSpaceController(WorkSpaceUseCase workSpaceUseCase) {
        this.workSpaceUseCase = workSpaceUseCase;
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<WorkSpaceResponse>> getWorkSpacesByAccountId(@PathVariable("accountId") String accountId) {

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

    @DeleteMapping("/{workSpaceId}")
    public ResponseEntity<WorkSpaceResponse> delete(@PathVariable("workSpaceId") String workSpaceId) {

        var workSpace = this.workSpaceUseCase.findById(workSpaceId);

        JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());

        this.workSpaceUseCase.delete(workSpace);

        return ResponseEntity.ok().build();
    }
}
