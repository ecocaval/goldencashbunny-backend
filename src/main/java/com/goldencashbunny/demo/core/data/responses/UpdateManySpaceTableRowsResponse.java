package com.goldencashbunny.demo.core.data.responses;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateManySpaceTableRowsResponse {

    private SpaceTableResponse table;

    private List<UpdateManySpaceTableRowsErrorMessage> errorMessages = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateManySpaceTableRowsErrorMessage {

        private String message;

        private Object details;
    }
}