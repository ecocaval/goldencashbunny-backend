package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.*;
import com.goldencashbunny.demo.core.data.responses.*;
import com.goldencashbunny.demo.core.usecases.SpaceUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.infra.security.JwtUtils;
import com.goldencashbunny.demo.presentation.entities.Space;
import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumnData;
import com.goldencashbunny.demo.presentation.exceptions.SpaceNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.SpaceTableNotFoundException;
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
@RequestMapping("v1")
public class SpaceController {

    private final WorkSpaceUseCase workSpaceUseCase;

    private final SpaceUseCase spaceUseCase;

    @Autowired
    public SpaceController(WorkSpaceUseCase workSpaceUseCase, SpaceUseCase spaceUseCase) {
        this.workSpaceUseCase = workSpaceUseCase;
        this.spaceUseCase = spaceUseCase;
    }

    @GetMapping("/workspace/{workSpaceId}/spaces")
    public ResponseEntity<List<SpaceResponse>> findByWorkSpaceId(@PathVariable("workSpaceId") String workSpaceId) {

        var workSpace = this.workSpaceUseCase.findById(workSpaceId);

        JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());

        var spaces = this.spaceUseCase.findByWorkSpaceId(workSpaceId);

        return ResponseEntity.status(HttpStatus.OK).body(
            spaces.stream().map(SpaceResponse::fromSpace).toList()
        );
    }

    @PostMapping("/workspace/{workSpaceId}/space")
    public ResponseEntity<SpaceResponse> create(
            @PathVariable("workSpaceId") String workSpaceId,
            @RequestBody @Valid CreateSpaceRequest request
    ) {
        var workSpace = this.workSpaceUseCase.findById(workSpaceId);

        JwtUtils.validateAdminRoleOrSameAccount(workSpace.getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
            SpaceResponse.fromSpace(this.spaceUseCase.create(request, workSpace))
        );
    }

    @PatchMapping("space/{spaceId}")
    public ResponseEntity<SpaceResponse> update(
            @PathVariable("spaceId") String spaceId,
            @RequestBody UpdateSpaceRequest request
    ) {
        var space = this.spaceUseCase.findById(spaceId);

        JwtUtils.validateAdminRoleOrSameAccount(space.getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
            SpaceResponse.fromSpace(this.spaceUseCase.update(request, space))
        );
    }

    @DeleteMapping("spaces/{spaceIds}")
    public ResponseEntity<DeleteSpacesResponse> deleteMany(@PathVariable("spaceIds") List<String> spaceIds) {

        var response = new DeleteSpacesResponse();

        Set<Space> spacesToDelete = new HashSet<>();

        for (String spaceId : spaceIds) {

            Space space = null;

            try {
                space = this.spaceUseCase.findById(spaceId);
            } catch (SpaceNotFoundException | BadRequestException ex) {
                response.getErrorMessages().add(
                    new DeleteSpacesResponse.DeleteSpaceErrorMessage(spaceId, ex.getMessage())
                );
                continue;
            }

            try {
                JwtUtils.validateAdminRoleOrSameAccount(space.getWorkspace().getAccount().getId());
            } catch (UnauthorizedException ex) {
                response.getErrorMessages().add(
                    new DeleteSpacesResponse.DeleteSpaceErrorMessage(spaceId, ex.getMessage())
                );
                continue;
            }

            spacesToDelete.add(space);
        }

        this.spaceUseCase.deleteMany(spacesToDelete);

        return ResponseEntity.ok(response);
    }

    @PostMapping("space/{spaceId}/table")
    public ResponseEntity<SpaceTableResponse> createTable(
            @PathVariable("spaceId") String spaceId,
            @RequestBody @Valid CreateSpaceTableRequest request
    ) {
        var space = this.spaceUseCase.findById(spaceId);

        JwtUtils.validateAdminRoleOrSameAccount(space.getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SpaceTableResponse.fromSpaceTable(this.spaceUseCase.createTable(request, space))
        );
    }

    @PatchMapping("space/table/{tableId}")
    public ResponseEntity<SpaceTableResponse> updateTable(
            @PathVariable("tableId") String tableId,
            @RequestBody UpdateSpaceTableRequest request
    ) {
        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                SpaceTableResponse.fromSpaceTable(this.spaceUseCase.updateTable(request, table))
        );
    }

    @DeleteMapping("space/tables/{tableIds}")
    public ResponseEntity<DeleteSpaceTablesResponse> deleteManyTables(@PathVariable("tableIds") List<String> tableIds) {

        var response = new DeleteSpaceTablesResponse();

        Set<SpaceTable> spaceTablesToDelete = new HashSet<>();

        for (String tableId : tableIds) {

            SpaceTable spaceTable = null;

            try {
                spaceTable = this.spaceUseCase.findTableById(tableId);
            } catch (SpaceTableNotFoundException | BadRequestException ex) {
                response.getErrorMessages().add(
                    new DeleteSpaceTablesResponse.DeleteSpaceTableErrorMessage(tableId, ex.getMessage())
                );
                continue;
            }

            try {
                JwtUtils.validateAdminRoleOrSameAccount(spaceTable.getSpace().getWorkspace().getAccount().getId());
            } catch (UnauthorizedException ex) {
                response.getErrorMessages().add(
                    new DeleteSpaceTablesResponse.DeleteSpaceTableErrorMessage(tableId, ex.getMessage())
                );
                continue;
            }

            spaceTablesToDelete.add(spaceTable);
        }

        this.spaceUseCase.deleteManyTables(spaceTablesToDelete);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/space/table/{tableId}/column")
    public ResponseEntity<SpaceTableColumnResponse> createTableColumn(
            @PathVariable("tableId") String tableId,
            @RequestBody @Valid CreateSpaceTableColumnRequest request
    ) {
        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
            SpaceTableColumnResponse.fromSpaceTableColumn(this.spaceUseCase.createColumn(request, table))
        );
    }

    @PatchMapping("/space/table/column/{columnId}")
    public ResponseEntity<List<SpaceTableColumnResponse>> updateTableColumn(
            @PathVariable("columnId") String columnId,
            @RequestBody @Valid UpdateSpaceTableColumnRequest request
    ) {
        var column = this.spaceUseCase.findColumnById(columnId);

        JwtUtils.validateAdminRoleOrSameAccount(column.getSpaceTable().getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                this.spaceUseCase.updateColumn(request, column)
                        .stream()
                        .map(SpaceTableColumnResponse::fromSpaceTableColumn)
                        .toList()
        );
    }

    @DeleteMapping("/space/table/column/{columnId}")
    public ResponseEntity<List<SpaceTableColumnResponse>> deleteTableColumn(
            @PathVariable("columnId") String columnId
    ) {
        var column = this.spaceUseCase.findColumnById(columnId);

        JwtUtils.validateAdminRoleOrSameAccount(column.getSpaceTable().getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                this.spaceUseCase.deleteColumn(column)
                        .stream()
                        .map(SpaceTableColumnResponse::fromSpaceTableColumn)
                        .toList()
        );
    }

    @PostMapping("/space/table/column/{columnId}/data")
    public ResponseEntity<SpaceTableColumnDataResponse> createTableColumData(
            @PathVariable("columnId") String columnId,
            @RequestBody @Valid CreateSpaceTableColumnDataRequest request
    ) {
        var column = this.spaceUseCase.findColumnById(columnId);

        JwtUtils.validateAdminRoleOrSameAccount(column.getSpaceTable().getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
            SpaceTableColumnDataResponse.fromSpaceTableColumnData(this.spaceUseCase.createColumnData(request, column))
        );
    }

//    @PatchMapping("space/table/{tableId}/rows")
//    public ResponseEntity<SpaceTableResponse> updateTableRowsPositions(
//            @PathVariable("tableId") String tableId,
//            @RequestBody UpdateSpaceTableRequest request
//    ) {
//        var table = this.spaceUseCase.findTableById(tableId);
//
//        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                SpaceTableResponse.fromSpaceTable(this.spaceUseCase.updateTable(request, table))
//        );
//    }

}
