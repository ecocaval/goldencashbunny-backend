package com.goldencashbunny.demo.core.data.responses;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteManyByReferenceResponse {

    private SpaceTableResponse table;

    private List<DeleteManyByReferenceErrorMessage> errorMessages = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteManyByReferenceErrorMessage {

        private Integer reference;
        private String details;
    }
}