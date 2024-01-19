package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.requests.*;
import com.goldencashbunny.demo.presentation.entities.*;

import java.util.List;
import java.util.Set;

public interface SpaceUseCase {

    List<Space> findByWorkSpaceId(String workSpaceId);

    Space create(CreateSpaceRequest request, Workspace workspace);

    Space update(UpdateSpaceRequest request, Space nonUpdatedSpace);

    Space findById(String spaceId);

    void deleteMany(Set<Space> spaces);

    SpaceTable findTableById(String tableId);

    SpaceTable createTable(CreateSpaceTableRequest request, Space space);

    SpaceTable updateTable(UpdateSpaceTableRequest request, SpaceTable nonUpdatedTable);

    void deleteManyTables(Set<SpaceTable> spaceTables);

    SpaceTableColumn findColumnById(String columnId);

    SpaceTableColumn createColumn(CreateSpaceTableColumnRequest request, SpaceTable table);

    List<SpaceTableColumn> updateColumn(UpdateSpaceTableColumnRequest request, SpaceTableColumn nonUpdatedColumn);

    void deleteManyColumns(Set<SpaceTableColumn> columns);

    SpaceTableColumnRow findColumnRowById(String rowId);

    SpaceTableColumnRow createColumnRow(CreateSpaceTableColumnRowRequest request, SpaceTableColumn column);

    SpaceTableColumnRow updateColumnRow(UpdateSpaceTableColumnRowRequest request, SpaceTableColumnRow nonUpdatedColumnRow);

    SpaceTable updateTableRowsReference(UpdateSpaceTableColumnRowReferenceRequest request, SpaceTable table);

//    void deleteManyColumnRows(Set<SpaceTableColumnRow> columnRows);

}
