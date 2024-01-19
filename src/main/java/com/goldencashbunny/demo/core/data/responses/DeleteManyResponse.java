package com.goldencashbunny.demo.core.data.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeleteManyResponse {

    private List<DeleteErrorMessage> errorMessages = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteErrorMessage {

        private String id;
        private String details;
    }
}
