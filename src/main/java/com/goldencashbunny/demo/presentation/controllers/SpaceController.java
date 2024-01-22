package com.goldencashbunny.demo.presentation.controllers;

import com.goldencashbunny.demo.core.data.requests.*;
import com.goldencashbunny.demo.core.data.responses.*;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.SpaceUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.infra.security.JwtUtils;
import com.goldencashbunny.demo.presentation.entities.Space;
import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumnRow;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<DeleteManyResponse> deleteMany(@PathVariable("spaceIds") List<String> spaceIds) {

        var response = new DeleteManyResponse();

        Set<Space> spacesToDelete = new HashSet<>();

        for (String spaceId : spaceIds) {

            Space space = null;

            try {
                space = this.spaceUseCase.findById(spaceId);
            } catch (SpaceNotFoundException | BadRequestException ex) {
                response.getErrorMessages().add(
                        new DeleteManyResponse.DeleteErrorMessage(spaceId, ex.getMessage())
                );
                continue;
            }

            try {
                JwtUtils.validateAdminRoleOrSameAccount(space.getWorkspace().getAccount().getId());
            } catch (UnauthorizedException ex) {
                response.getErrorMessages().add(
                        new DeleteManyResponse.DeleteErrorMessage(spaceId, ex.getMessage())
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
    public ResponseEntity<DeleteManyResponse> deleteManyTables(@PathVariable("tableIds") List<String> tableIds) {

        var response = new DeleteManyResponse();

        Set<SpaceTable> spaceTablesToDelete = new HashSet<>();

        for (String tableId : tableIds) {

            SpaceTable spaceTable = null;

            try {
                spaceTable = this.spaceUseCase.findTableById(tableId);
            } catch (SpaceTableNotFoundException | BadRequestException ex) {
                response.getErrorMessages().add(
                        new DeleteManyResponse.DeleteErrorMessage(tableId, ex.getMessage())
                );
                continue;
            }

            try {
                JwtUtils.validateAdminRoleOrSameAccount(spaceTable.getSpace().getWorkspace().getAccount().getId());
            } catch (UnauthorizedException ex) {
                response.getErrorMessages().add(
                        new DeleteManyResponse.DeleteErrorMessage(tableId, ex.getMessage())
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

    @DeleteMapping("/space/table/{tableId}/column/references/{columnReferences}")
    public ResponseEntity<DeleteManyByReferenceResponse> deleteTableColumns(
            @PathVariable("tableId") String tableId,
            @PathVariable("columnReferences") List<Integer> columnReferences
    ) {
        var response = new DeleteManyByReferenceResponse();

        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        Set<SpaceTableColumn> columnsToDelete = table.getColumns()
                .stream()
                .filter(column -> columnReferences.contains(column.getColumnReference()))
                .collect(Collectors.toSet());

        Set<Integer> foundColumnReferences = columnsToDelete.stream()
                .map(SpaceTableColumn::getColumnReference)
                .collect(Collectors.toSet());

        columnReferences.stream()
                .filter(columnReference -> !foundColumnReferences.contains(columnReference))
                .map(columnReference -> new DeleteManyByReferenceResponse.DeleteManyByReferenceErrorMessage(
                        columnReference,
                        ErrorMessages.ERROR_SPACE_TABLE_COLUMN_NOT_FOUND_BY_REFERENCE.getMessage()
                ))
                .forEach(response.getErrorMessages()::add);

        table.getColumns().removeAll(columnsToDelete);

        this.spaceUseCase.deleteManyColumns(columnsToDelete);

        response.setColumns(table.getColumns().stream().map(SpaceTableColumnResponse::fromSpaceTableColumn).toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/space/table/column/{columnId}/row")
    public ResponseEntity<SpaceTableColumnRowResponse> createTableColumRow(
            @PathVariable("columnId") String columnId,
            @RequestBody @Valid CreateSpaceTableColumnRowRequest request
    ) {
        var column = this.spaceUseCase.findColumnById(columnId);

        JwtUtils.validateAdminRoleOrSameAccount(column.getSpaceTable().getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SpaceTableColumnRowResponse.fromSpaceTableColumnRow(this.spaceUseCase.createColumnRow(request, column))
        );
    }

    @PatchMapping("/space/table/column/row/{rowId}")
    public ResponseEntity<SpaceTableColumnRowResponse> updateTableColumRow(
            @PathVariable("rowId") String rowId,
            @RequestBody UpdateSpaceTableColumnRowRequest request
    ) {
        var row = this.spaceUseCase.findColumnRowById(rowId);

        JwtUtils.validateAdminRoleOrSameAccount(
                row.getSpaceTableColumn().getSpaceTable().getSpace().getWorkspace().getAccount().getId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                SpaceTableColumnRowResponse.fromSpaceTableColumnRow(this.spaceUseCase.updateColumnRow(request, row))
        );
    }

    @PatchMapping("/space/table/{tableId}/rows")
    public ResponseEntity<SpaceTableResponse> updateTableRowsPositions(
            @PathVariable("tableId") String tableId,
            @RequestBody UpdateSpaceTableColumnRowReferenceRequest request
    ) {
        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                SpaceTableResponse.fromSpaceTable(this.spaceUseCase.updateTableRowsReference(request, table))
        );
    }

    @DeleteMapping("/space/table/{tableId}/row/references/{rowReferences}")
    public ResponseEntity<DeleteManyByReferenceResponse> deleteTableRows(
            @PathVariable("tableId") String tableId,
            @PathVariable("rowReferences") List<Integer> rowReferences
    ) {
        var response = new DeleteManyByReferenceResponse();

        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        Set<SpaceTableColumnRow> rowsToDelete = new HashSet<>();

        table.getColumns().forEach(column -> column.getSpaceTableColumnRows()
                .stream()
                .filter(row -> rowReferences.contains(row.getRowReference()))
                .forEach(rowsToDelete::add));

        Set<Integer> foundRowReferences = rowsToDelete.stream()
                .map(SpaceTableColumnRow::getRowReference)
                .collect(Collectors.toSet());

        rowReferences.stream()
                .filter(rowReference -> !foundRowReferences.contains(rowReference))
                .map(rowReference -> new DeleteManyByReferenceResponse.DeleteManyByReferenceErrorMessage(
                        rowReference,
                        ErrorMessages.ERROR_SPACE_TABLE_ROW_NOT_FOUND_BY_REFERENCE.getMessage()
                ))
                .forEach(response.getErrorMessages()::add);

        table.getColumns().forEach(column -> {
            column.getSpaceTableColumnRows().removeAll(rowsToDelete);
        });

        this.spaceUseCase.deleteManyRows(rowsToDelete, table.getColumns());

        response.setColumns(
                table.getColumns()
                        .stream()
                        .map(SpaceTableColumnResponse::fromSpaceTableColumn).toList()
        );

        return ResponseEntity.ok(response);
    }
}
