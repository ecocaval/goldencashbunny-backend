package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.enums.SpaceTableColumnType;
import com.goldencashbunny.demo.core.data.requests.*;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.SpaceUseCase;
import com.goldencashbunny.demo.core.utils.SpaceTableUtils;
import com.goldencashbunny.demo.core.utils.UuidUtils;
import com.goldencashbunny.demo.presentation.entities.*;
import com.goldencashbunny.demo.presentation.exceptions.*;
import com.goldencashbunny.demo.presentation.repositories.SpaceRepository;
import com.goldencashbunny.demo.presentation.repositories.SpaceTableColumnRowRepository;
import com.goldencashbunny.demo.presentation.repositories.SpaceTableColumnRepository;
import com.goldencashbunny.demo.presentation.repositories.SpaceTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class SpaceUseCaseImpl implements SpaceUseCase {

    private final SpaceRepository spaceRepository;

    private final SpaceTableRepository spaceTableRepository;

    private final SpaceTableColumnRepository spaceTableColumnRepository;

    private final SpaceTableColumnRowRepository spaceTableColumnRowRepository;

    @Autowired
    public SpaceUseCaseImpl(
            SpaceRepository spaceRepository,
            SpaceTableRepository spaceTableRepository,
            SpaceTableColumnRepository spaceTableColumnRepository,
            SpaceTableColumnRowRepository spaceTableColumnRowRepository
    ) {
        this.spaceRepository = spaceRepository;
        this.spaceTableRepository = spaceTableRepository;
        this.spaceTableColumnRepository = spaceTableColumnRepository;
        this.spaceTableColumnRowRepository = spaceTableColumnRowRepository;
    }

    @Override
    public List<Space> findByWorkSpaceId(String workSpaceId) {

        return this.spaceRepository.findByWorkspaceIdOrderByIsFavoriteDesc(
                UuidUtils.getValidUuidFromString(workSpaceId, ErrorMessages.ERROR_INVALID_WORKSPACE_ID.getMessage())
        );
    }

    @Override
    @Transactional
    public Space create(CreateSpaceRequest request, Workspace workspace) {

        var space = Space.fromCreateSpaceRequest(request, workspace);

        return this.spaceRepository.save(space);
    }

    @Override
    @Transactional
    public Space update(UpdateSpaceRequest request, Space nonUpdatedSpace) {
        return this.spaceRepository.save(Space.fromUpdateSpaceRequest(request, nonUpdatedSpace));
    }

    @Override
    public Space findById(String spaceId) {

        return this.spaceRepository.findById(
                UuidUtils.getValidUuidFromString(spaceId, ErrorMessages.ERROR_INVALID_SPACE_ID.getMessage())
        ).orElseThrow(() -> new SpaceNotFoundException(ErrorMessages.ERROR_SPACE_NOT_FOUND_BY_ID.getMessage()));
    }

    @Override
    @Transactional
    public void deleteMany(Set<Space> spaces) {

        if (spaces.isEmpty()) {
            return;
        }

        this.spaceRepository.deleteAll(spaces);
    }

    @Override
    public SpaceTable findTableById(String tableId) {

        return this.spaceTableRepository.findById(
                UuidUtils.getValidUuidFromString(tableId, ErrorMessages.ERROR_INVALID_SPACE_TABLE_ID.getMessage())
        ).orElseThrow(() -> new SpaceTableNotFoundException(ErrorMessages.ERROR_SPACE_TABLE_NOT_FOUND_BY_ID.getMessage()));
    }

    @Override
    @Transactional
    public SpaceTable createTable(CreateSpaceTableRequest request, Space space) {

        var spaceTable = SpaceTable.fromCreateSpaceTableRequest(request, space);

        return this.spaceTableRepository.save(spaceTable);
    }

    @Override
    @Transactional
    public SpaceTable updateTable(UpdateSpaceTableRequest request, SpaceTable nonUpdatedTable) {
        return this.spaceTableRepository.save(SpaceTable.fromUpdateSpaceTableRequest(request, nonUpdatedTable));
    }

    @Override
    @Transactional
    public void deleteManyTables(Set<SpaceTable> spaceTables) {

        if (spaceTables.isEmpty()) {
            return;
        }

        this.spaceTableRepository.deleteAll(spaceTables);
    }

    @Override
    public SpaceTableColumn findColumnById(String columnId) {

        return this.spaceTableColumnRepository.findById(
                UuidUtils.getValidUuidFromString(columnId, ErrorMessages.ERROR_INVALID_SPACE_TABLE_COLUMN_ID.getMessage())
        ).orElseThrow(() -> new SpaceTableColumnNotFoundException(ErrorMessages.ERROR_SPACE_TABLE_COLUMN_NOT_FOUND_BY_ID.getMessage()));
    }

    @Override
    @Transactional
    public SpaceTableColumn createColumn(CreateSpaceTableColumnRequest request, SpaceTable table) {

        var column = SpaceTableColumn.fromCreateSpaceTableColumnRequest(request, table);

        var columnReference = this.spaceTableColumnRepository.findMaxValueOfColumnReferenceByTable(table);

        if (columnReference != null) {
            columnReference++;
        } else {
            columnReference = 0;
        }

        column.setColumnReference(columnReference);

        return this.spaceTableColumnRepository.save(column);
    }

    @Override
    @Transactional
    public List<SpaceTableColumn> updateColumn(UpdateSpaceTableColumnRequest request, SpaceTableColumn nonUpdatedColumn) {

        var updatedColumn = SpaceTableColumn.fromUpdateSpaceTableColumnRequest(request, nonUpdatedColumn);

        if (request.getColumnReference() == null) {
            return List.of(this.spaceTableColumnRepository.save(updatedColumn));
        }

        validateUpdatedColumnReference(request.getColumnReference(), nonUpdatedColumn);

        var allRemainingColumns = this.spaceTableColumnRepository.findBySpaceTableIdAndColumnReferenceNotIn(
                updatedColumn.getSpaceTable().getId(), Collections.singleton(nonUpdatedColumn.getColumnReference())
        );

        adjustColumnsReferencesDueToUpdate(updatedColumn, nonUpdatedColumn, allRemainingColumns);

        allRemainingColumns.add(updatedColumn);

        return this.spaceTableColumnRepository.saveAll(allRemainingColumns);
    }

    @Override
    @Transactional
    public void deleteManyColumns(Set<SpaceTableColumn> columns) {

        if (columns.isEmpty()) {
            return;
        }

        var allRemainingColumnsSortedByColumnReference =
                this.spaceTableColumnRepository.findBySpaceTableIdAndColumnReferenceNotInOrderByColumnReferenceAsc(
                        columns.stream().toList().get(0).getSpaceTable().getId(),
                        columns.stream().map(SpaceTableColumn::getColumnReference).toList()
                );

        this.spaceTableColumnRepository.deleteAll(columns);

        adjustColumnsReferencesDueToDelete(allRemainingColumnsSortedByColumnReference);

        this.spaceTableColumnRepository.saveAll(allRemainingColumnsSortedByColumnReference);
    }

    @Override
    public SpaceTableColumnRow findColumnRowById(String rowId) {

        return this.spaceTableColumnRowRepository.findById(
                UuidUtils.getValidUuidFromString(rowId, ErrorMessages.ERROR_INVALID_SPACE_TABLE_COLUMN_ROW_ID.getMessage())
        ).orElseThrow(() -> new SpaceTableColumnRowNotFoundException(ErrorMessages.ERROR_SPACE_TABLE_COLUMN_ROW_NOT_FOUND_BY_ID.getMessage()));
    }


    @Override
    @Transactional
    public SpaceTableColumnRow createColumnRow(CreateSpaceTableColumnRowRequest request, SpaceTableColumn column) {

        if (request.getRowReference() < 0) {
            throw new InvalidRowReferenceException(ErrorMessages.ERROR_INVALID_ROW_REFERENCE.getMessage());
        }

        var rowIsAlreadyOccupied = this.spaceTableColumnRowRepository.existsByRowReferenceAndSpaceTableColumnId(
                request.getRowReference(), column.getId()
        );

        if (rowIsAlreadyOccupied) {
            throw new InvalidRowReferenceException(ErrorMessages.ERROR_INVALID_ROW_REFERENCE.getMessage());
        }

        validateValueAccordingToColumnType(request.getValue(), column.getColumnType());

        request.setValue(adjustValueAccordingToColumnType(request.getValue(), column.getColumnType()));

        return this.spaceTableColumnRowRepository.save(SpaceTableColumnRow.fromCreateSpaceTableColumnRowRequest(request, column));
    }

    @Override
    @Transactional
    public SpaceTableColumnRow updateColumnRow(
            UpdateSpaceTableColumnRowRequest request, SpaceTableColumnRow nonUpdatedColumnRow
    ) {
        validateValueAccordingToColumnType(
                request.getValue(), nonUpdatedColumnRow.getSpaceTableColumn().getColumnType()
        );

        request.setValue(adjustValueAccordingToColumnType(
                request.getValue(), nonUpdatedColumnRow.getSpaceTableColumn().getColumnType())
        );

        return this.spaceTableColumnRowRepository.save(
                SpaceTableColumnRow.fromUpdateSpaceTableColumnRowRequest(request, nonUpdatedColumnRow)
        );
    }

    @Override
    @Transactional
    public SpaceTable updateTableRowsReference(
            UpdateSpaceTableColumnRowReferenceRequest request, SpaceTable table
    ) {
        int oldRowReference = request.getFrom();
        int newRowReference = request.getTo();

        List<SpaceTableColumnRow> rowsWithOldRowReference = new ArrayList<>();

        List<SpaceTableColumnRow> allRemainingRows = new ArrayList<>();

        for (SpaceTableColumn column : table.getColumns()) {

            for (SpaceTableColumnRow row : column.getSpaceTableColumnRows()) {

                if (row.getRowReference().equals(oldRowReference)) {
                    rowsWithOldRowReference.add(row);
                } else {
                    allRemainingRows.add(row);
                }
            }
        }

        if (rowsWithOldRowReference.isEmpty()) {
            throw new InvalidRowReferenceException(ErrorMessages.ERROR_INVALID_FROM_ROW_REFERENCE.getMessage());
        }

        rowsWithOldRowReference.forEach(row -> row.setRowReference(newRowReference));

        adjustRowsReferencesDueToUpdate(newRowReference, oldRowReference, allRemainingRows);

        allRemainingRows.addAll(rowsWithOldRowReference);

        return table;
    }

    private void validateUpdatedColumnReference(Integer columnReference, SpaceTableColumn nonUpdatedColumn) {

        var columnReferenceMaxValue = this.spaceTableColumnRepository.findMaxValueOfColumnReferenceByTable(
                nonUpdatedColumn.getSpaceTable()
        );

        if (columnReference > columnReferenceMaxValue || columnReference < 0) {
            throw new InvalidColumnReferenceException(columnReference, columnReferenceMaxValue);
        }
    }

    private void adjustColumnsReferencesDueToUpdate(
            SpaceTableColumn updatedColumn, SpaceTableColumn nonUpdatedColumn, List<SpaceTableColumn> allRemainingColumns
    ) {
        if (updatedColumn.getColumnReference() < nonUpdatedColumn.getColumnReference()) {
            allRemainingColumns.forEach(column -> {
                if (column.getColumnReference() >= updatedColumn.getColumnReference() && column.getColumnReference() < nonUpdatedColumn.getColumnReference())
                    column.setColumnReference(column.getColumnReference() + 1);
            });

        } else {
            allRemainingColumns.forEach(column -> {
                if (column.getColumnReference() <= updatedColumn.getColumnReference() && column.getColumnReference() > nonUpdatedColumn.getColumnReference())
                    column.setColumnReference(column.getColumnReference() - 1);
            });
        }
    }

    private void adjustColumnsReferencesDueToDelete(
            List<SpaceTableColumn> allRemainingColumnsSortedByColumnReference
    ) {
        for (int i = 0; i < allRemainingColumnsSortedByColumnReference.size(); i++) {

            int newColumnReference = allRemainingColumnsSortedByColumnReference.get(i).getColumnReference();

            if (i == 0) {
                while (newColumnReference > 0) {
                    newColumnReference--;
                }
                allRemainingColumnsSortedByColumnReference.get(i).setColumnReference(newColumnReference);

            } else {
                while (newColumnReference > allRemainingColumnsSortedByColumnReference.get(i - 1).getColumnReference() + 1) {
                    newColumnReference--;
                }
                allRemainingColumnsSortedByColumnReference.get(i).setColumnReference(newColumnReference);
            }
        }
    }

    private void validateValueAccordingToColumnType(String value, SpaceTableColumnType columnType) {

        switch (columnType) {

            case CHECKBOX:
                if (!SpaceTableUtils.checkIfValueIsBoolean(value))
                    throw new InvalidValueForColumnTypeException(
                            ErrorMessages.ERROR_INVALID_VALUE_FOR_COLUMN_TYPE.getMessage(), value, columnType.name()
                    );
                break;

            case DATE:
                if (!SpaceTableUtils.checkIfValueIsDate(value))
                    throw new InvalidValueForColumnTypeException(
                            ErrorMessages.ERROR_INVALID_VALUE_FOR_COLUMN_TYPE.getMessage(), value, columnType.name()
                    );
                break;

            case NUMBER:
                if (!SpaceTableUtils.checkIfValueIsNumber(value))
                    throw new InvalidValueForColumnTypeException(
                            ErrorMessages.ERROR_INVALID_VALUE_FOR_COLUMN_TYPE.getMessage(), value, columnType.name()
                    );
                break;

            // case TEXT: if the value is text anything will be accepted as value...
        }
        ;
    }

    private String adjustValueAccordingToColumnType(String value, SpaceTableColumnType columnType) {
        switch (columnType) {
            case DATE -> {
                return SpaceTableUtils.convertStringToDefaultDatePattern(value);
            }
            case CHECKBOX -> {
                return Boolean.valueOf(value).toString();
            }
            default -> {
                return value;
            }
        }
    }

    private void adjustRowsReferencesDueToUpdate(
            int newRowReference, int oldRowReference, List<SpaceTableColumnRow> allRemainingRows
    ) {
        if (newRowReference < oldRowReference) {
            allRemainingRows.forEach(row -> {
                if (row.getRowReference() >= newRowReference && row.getRowReference() < oldRowReference)
                    row.setRowReference(row.getRowReference() + 1);
            });

        } else {
            allRemainingRows.forEach(row -> {
                if (row.getRowReference() <= newRowReference && row.getRowReference() > oldRowReference)
                    row.setRowReference(row.getRowReference() - 1);
            });
        }
    }
}
