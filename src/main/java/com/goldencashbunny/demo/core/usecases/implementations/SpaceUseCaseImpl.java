package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.core.data.requests.*;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.SpaceUseCase;
import com.goldencashbunny.demo.core.utils.UuidUtils;
import com.goldencashbunny.demo.presentation.entities.Space;
import com.goldencashbunny.demo.presentation.entities.SpaceTable;
import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import com.goldencashbunny.demo.presentation.exceptions.InvalidColumnReferenceException;
import com.goldencashbunny.demo.presentation.exceptions.SpaceNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.SpaceTableNotFoundException;
import com.goldencashbunny.demo.presentation.repositories.SpaceRepository;
import com.goldencashbunny.demo.presentation.repositories.SpaceTableColumnRepository;
import com.goldencashbunny.demo.presentation.repositories.SpaceTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class SpaceUseCaseImpl implements SpaceUseCase {

    private final SpaceRepository spaceRepository;

    private final SpaceTableRepository spaceTableRepository;

    private final SpaceTableColumnRepository spaceTableColumnRepository;

    @Autowired
    public SpaceUseCaseImpl(
            SpaceRepository spaceRepository,
            SpaceTableRepository spaceTableRepository,
            SpaceTableColumnRepository spaceTableColumnRepository
    ) {
        this.spaceRepository = spaceRepository;
        this.spaceTableRepository = spaceTableRepository;
        this.spaceTableColumnRepository = spaceTableColumnRepository;
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
        this.spaceTableRepository.deleteAll(spaceTables);
    }

    @Override
    public SpaceTableColumn findColumnById(String columnId) {

        return this.spaceTableColumnRepository.findById(
                UuidUtils.getValidUuidFromString(columnId, ErrorMessages.ERROR_INVALID_SPACE_TABLE_COLUMN_ID.getMessage())
        ).orElseThrow(() -> new SpaceNotFoundException(ErrorMessages.ERROR_SPACE_TABLE_COLUMN_NOT_FOUND_BY_ID.getMessage()));
    }

    @Override
    @Transactional
    public SpaceTableColumn createColumn(CreateSpaceTableColumnRequest request, SpaceTable table) {

        var column = SpaceTableColumn.fromCreateSpaceTableColumnRequest(request, table);

        var columnReference = this.spaceTableColumnRepository.findMaxValueOfColumnReference(table);

        if(columnReference != null) {
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

        if(request.getColumnReference() == null) {
            return List.of(this.spaceTableColumnRepository.save(updatedColumn));
        }

        validateUpdatedColumnReference(request.getColumnReference(), nonUpdatedColumn);

        var allRemainingColumns = this.spaceTableColumnRepository.findBySpaceTableIdAndColumnReferenceNotIn(
            updatedColumn.getSpaceTable().getId(), Collections.singleton(nonUpdatedColumn.getColumnReference())
        );

        adjustColumnsDueToUpdate(updatedColumn, nonUpdatedColumn, allRemainingColumns);

        allRemainingColumns.add(updatedColumn);

        return this.spaceTableColumnRepository.saveAll(allRemainingColumns);
    }

    @Override
    @Transactional
    public List<SpaceTableColumn> deleteColumn(SpaceTableColumn column) {

        var allRemainingColumns = this.spaceTableColumnRepository.findBySpaceTableIdAndColumnReferenceNotIn(
            column.getSpaceTable().getId(), Collections.singleton(column.getColumnReference())
        );

        this.spaceTableColumnRepository.delete(column);

        adjustColumnsDueToDelete(column, allRemainingColumns);

        return this.spaceTableColumnRepository.saveAll(allRemainingColumns);
    }

    private void validateUpdatedColumnReference(Integer columnReference, SpaceTableColumn nonUpdatedColumn) {

        var columnReferenceMaxValue = this.spaceTableColumnRepository.findMaxValueOfColumnReference(
                nonUpdatedColumn.getSpaceTable()
        );

        if(columnReference > columnReferenceMaxValue || columnReference < 0) {
            throw new InvalidColumnReferenceException(columnReference, columnReferenceMaxValue);
        }
    }

    private void adjustColumnsDueToUpdate(
        SpaceTableColumn updatedColumn, SpaceTableColumn nonUpdatedColumn, List<SpaceTableColumn> allRemainingColumns
    ) {
        if(updatedColumn.getColumnReference() < nonUpdatedColumn.getColumnReference()) {
            allRemainingColumns.forEach(column -> {
                if(column.getColumnReference() >= updatedColumn.getColumnReference() && column.getColumnReference() < nonUpdatedColumn.getColumnReference())
                    column.setColumnReference(column.getColumnReference() + 1);
            });

        } else {
            allRemainingColumns.forEach(column -> {
                if(column.getColumnReference() <= updatedColumn.getColumnReference() && column.getColumnReference() > nonUpdatedColumn.getColumnReference())
                    column.setColumnReference(column.getColumnReference() - 1);
            });
        }
    }

    private void adjustColumnsDueToDelete(
        SpaceTableColumn deletedColumn, List<SpaceTableColumn> allRemainingColumns
    ) {
        allRemainingColumns.forEach(column -> {
            if(column.getColumnReference() > deletedColumn.getColumnReference())
                column.setColumnReference(column.getColumnReference() - 1);
        });
    }
}
