package com.goldencashbunny.demo.core.data.responses;

import com.goldencashbunny.demo.presentation.entities.SpaceTableColumn;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteManyColumnsResponse {

    private List<SpaceTableColumnResponse> columns = new ArrayList<>();

    private List<DeleteManyColumnsErrorMessage> errorMessages = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteManyColumnsErrorMessage {

        private Integer reference;
        private String details;
    }
}