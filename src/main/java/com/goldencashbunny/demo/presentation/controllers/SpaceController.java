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
import com.goldencashbunny.demo.presentation.exceptions.InvalidValueForColumnTypeException;
import com.goldencashbunny.demo.presentation.exceptions.SpaceNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.SpaceTableNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.base.BadRequestException;
import com.goldencashbunny.demo.presentation.exceptions.base.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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

        return ResponseEntity.status(HttpStatus.OK).body(
                this.spaceUseCase.findByWorkSpaceId(workSpaceId).stream().map(SpaceResponse::fromSpace).toList()
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

    @GetMapping("space/table/{tableId}")
    public ResponseEntity<SpaceTableResponse> findTableById(
            @PathVariable("tableId") String tableId
    ) {
        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                SpaceTableResponse.fromSpaceTable(table)
        );
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

        response.setTable(SpaceTableResponse.fromSpaceTable(table));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/space/table/{tableId}/rows")
    public ResponseEntity<SpaceTableResponse> createTableColumRow(
            @PathVariable("tableId") String tableId,
            @RequestBody List<CreateSpaceTableColumnRowRequest> requests
    ) {
        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        var columns = table.getColumns()
                .stream()
                .sorted(Comparator.comparing(SpaceTableColumn::getColumnReference))
                .toList();

        requests.forEach(request -> {
                    try {
                        this.spaceUseCase.createColumnRow(request, columns.get(request.getColumnReference()));
                    } catch (IndexOutOfBoundsException ignore) {
                    }
                }
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SpaceTableResponse.fromSpaceTable(this.spaceUseCase.findTableById(tableId))
        );
    }

    @PatchMapping("/space/table/{tableId}/rows")
    public ResponseEntity<UpdateManySpaceTableRowsResponse> updateTableColumRow(
            @PathVariable("tableId") String tableId,
            @RequestBody List<UpdateSpaceTableColumnRowRequest> requests
    ) {
        var table = this.spaceUseCase.findTableById(tableId);

        JwtUtils.validateAdminRoleOrSameAccount(table.getSpace().getWorkspace().getAccount().getId());

        var columns = table.getColumns()
                .stream()
                .sorted(Comparator.comparing(SpaceTableColumn::getColumnReference))
                .toList();

        var response = new UpdateManySpaceTableRowsResponse();

        requests.forEach(request -> {
                final var rows = columns.get(request.getColumnReference())
                        .getSpaceTableColumnRows()
                        .stream()
                        .sorted(Comparator.comparing(SpaceTableColumnRow::getRowReference))
                        .toList();
                try {
                    this.spaceUseCase.updateColumnRow(request, rows.get(request.getRowReference()));
                } catch (IndexOutOfBoundsException ignore) {

                } catch (InvalidValueForColumnTypeException ex) {
                    response.getErrorMessages().add(
                        new UpdateManySpaceTableRowsResponse.UpdateManySpaceTableRowsErrorMessage(ex.getMessage(), ex.getDetails())
                    );
                }
            }
        );

        response.setTable(SpaceTableResponse.fromSpaceTable(this.spaceUseCase.findTableById(tableId)));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/space/table/{tableId}/row")
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

        response.setTable(SpaceTableResponse.fromSpaceTable(table));

        return ResponseEntity.ok(response);
    }
}
